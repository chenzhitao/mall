import request from '@/utils/request'

export function add(data) {
  return request({
    url: 'api/yxProductTemplate',
    method: 'post',
    data
  })
}

export function del(ids) {
  return request({
    url: 'api/yxProductTemplate/',
    method: 'delete',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'api/yxProductTemplate',
    method: 'put',
    data
  })
}

export function addItems(data) {
  return request({
    url: 'api/yxProductTemplate/addTemplateItems',
    method: 'post',
    data
  })
}

export function updateItems(data) {
  return request({
    url: 'api/yxProductTemplate/editTemplateItems',
    method: 'post',
    data
  })
}

export default { add, edit, del,addItems }
