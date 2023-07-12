package co.yixiang.modules.system.repository;

import co.yixiang.modules.system.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import java.util.Set;

/**
 * @author Zheng Jie
 * @date 2018-12-03
 */
@SuppressWarnings("all")
public interface RoleRepository extends JpaRepository<Role, Long>, JpaSpecificationExecutor<Role> {

    /**
     * 根据名称查询
     * @param name /
     * @return /
     */
    Role findByName(String name);

    /**
     * 根据用户ID查询
     * @param id 用户ID
     * @return
     */
    Set<Role> findByUsers_Id(Long id);

    /**
     * 解绑角色菜单
     * @param id 菜单ID
     */
    @Modifying
    @Query(value = "delete from roles_menus where menu_id = ?1",nativeQuery = true)
    void untiedMenu(Long id);

    /**
     * 根据角色权限查询
     * @param permission /
     * @return /
     */
    Role findByPermission(String permission);

    /**
     * 根据部门查询
     * @param deptIds /
     * @return /
     */
    @Query(value = "select count(1) from role r, roles_depts d where " +
            "r.role_id = d.role_id and d.dept_id in ?1",nativeQuery = true)
    int countByDepts(Set<Long> deptIds);
}
