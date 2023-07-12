import request from '@/utils/request'

export function add(data) {
  return request({
    url: 'api/yxProductTemplateItem',
    method: 'post',
    data
  })
}

export function del(ids) {
  return request({
    url: 'api/yxProductTemplateItem/',
    method: 'delete',
    data: ids
  })
}

export function delItems(ids) {
  return request({
    url: 'api/yxProductTemplateItem/',
    method: 'delete',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'api/yxProductTemplateItem',
    method: 'put',
    data
  })
}

export default { add, edit, del,delItems }
