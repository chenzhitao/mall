package co.yixiang.modules.system.service.impl;

import co.yixiang.exception.BadRequestException;
import co.yixiang.modules.system.repository.UserRepository;
import co.yixiang.modules.system.service.dto.RoleDTO;
import co.yixiang.modules.system.service.dto.RoleSmallDTO;
import co.yixiang.modules.system.service.dto.UserDTO;
import co.yixiang.modules.system.domain.Menu;
import co.yixiang.modules.system.domain.Role;
import co.yixiang.exception.EntityExistException;
import co.yixiang.modules.system.repository.RoleRepository;
import co.yixiang.modules.system.service.RoleService;
import co.yixiang.modules.system.service.dto.RoleQueryCriteria;
import co.yixiang.modules.system.service.mapper.RoleMapper;
import co.yixiang.modules.system.service.mapper.RoleSmallMapper;
import co.yixiang.utils.*;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Zheng Jie
 * @date 2018-12-03
 */
@Service
@CacheConfig(cacheNames = "role")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    private final RoleMapper roleMapper;

    private final RoleSmallMapper roleSmallMapper;

    private final UserRepository userRepository;

    public RoleServiceImpl(RoleRepository roleRepository, RoleMapper roleMapper, RoleSmallMapper roleSmallMapper, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
        this.roleSmallMapper = roleSmallMapper;
        this.userRepository = userRepository;
    }

    @Override
    @Cacheable
    public Object queryAll(Pageable pageable) {
        return roleMapper.toDto(roleRepository.findAll(pageable).getContent());
    }

    @Override
    @Cacheable
    public List<RoleDTO> queryAll(RoleQueryCriteria criteria) {
        return roleMapper.toDto(roleRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Cacheable
    public Object queryAll(RoleQueryCriteria criteria, Pageable pageable) {
        Page<Role> page = roleRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(roleMapper::toDto));
    }

    @Override
    @Cacheable(key = "#p0")
    public RoleDTO findById(long id) {
        Role role = roleRepository.findById(id).orElseGet(Role::new);
        ValidationUtil.isNull(role.getId(),"Role","id",id);
        return roleMapper.toDto(role);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public RoleDTO create(Role resources) {
        if(roleRepository.findByName(resources.getName()) != null){
            throw new EntityExistException(Role.class,"username",resources.getName());
        }
        return roleMapper.toDto(roleRepository.save(resources));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(Role resources) {
        Role role = roleRepository.findById(resources.getId()).orElseGet(Role::new);
        ValidationUtil.isNull(role.getId(),"Role","id",resources.getId());

        Role role1 = roleRepository.findByName(resources.getName());

        if(role1 != null && !role1.getId().equals(role.getId())){
            throw new EntityExistException(Role.class,"username",resources.getName());
        }
        role1 = roleRepository.findByPermission(resources.getPermission());
        if(role1 != null && !role1.getId().equals(role.getId())){
            throw new EntityExistException(Role.class,"permission",resources.getPermission());
        }
        role.setName(resources.getName());
        role.setRemark(resources.getRemark());
        role.setDataScope(resources.getDataScope());
        role.setDepts(resources.getDepts());
        role.setLevel(resources.getLevel());
        role.setPermission(resources.getPermission());
        roleRepository.save(role);
    }

    @Override
    @CacheEvict(allEntries = true)
    public void updateMenu(Role resources, RoleDTO roleDTO) {
        Role role = roleMapper.toEntity(roleDTO);
        role.setMenus(resources.getMenus());
        roleRepository.save(role);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void untiedMenu(Long id) {
        roleRepository.untiedMenu(id);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        for (Long id : ids) {
            roleRepository.deleteById(id);
        }
    }

    @Override
    @Cacheable(key = "'findByUsers_Id:' + #p0")
    public List<RoleSmallDTO> findByUsersId(Long id) {
        return roleSmallMapper.toDto(new ArrayList<>(roleRepository.findByUsers_Id(id)));
    }

    @Override
    @Cacheable
    public Integer findByRoles(Set<Role> roles) {
        Set<RoleDTO> roleDtos = new HashSet<>();
        for (Role role : roles) {
            roleDtos.add(findById(role.getId()));
        }
        return Collections.min(roleDtos.stream().map(RoleDTO::getLevel).collect(Collectors.toList()));
    }

    @Override
    @Cacheable(key = "'loadPermissionByUser:' + #p0.username")
    public Collection<GrantedAuthority> mapToGrantedAuthorities(UserDTO user) {
        Set<Role> roles = roleRepository.findByUsers_Id(user.getId());
        Set<String> permissions = roles.stream().filter(role -> StringUtils.isNotBlank(role.getPermission())).map(Role::getPermission).collect(Collectors.toSet());
        permissions.addAll(
                roles.stream().flatMap(role -> role.getMenus().stream())
                        .filter(menu -> StringUtils.isNotBlank(menu.getPermission()))
                        .map(Menu::getPermission).collect(Collectors.toSet())
        );
        return permissions.stream().map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    /**
     * 验证是否被用户关联
     *
     * @param ids /
     */
    @Override
    public void verification(Set<Long> ids) {
        if (userRepository.countByRoles(ids) > 0) {
            throw new BadRequestException("所选角色存在用户关联，请解除关联再试！");
        }
    }

    @Override
    public void download(List<RoleDTO> roles, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (RoleDTO role : roles) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("角色名称", role.getName());
            map.put("默认权限", role.getPermission());
            map.put("角色级别", role.getLevel());
            map.put("描述", role.getRemark());
            map.put("创建日期", role.getCreateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
