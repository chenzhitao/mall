import request from '@/utils/request'

export function add(data) {
  return request({
    url: 'api/yxVideoProduct',
    method: 'post',
    data
  })
}

export function del(ids) {
  return request({
    url: 'api/yxVideoProduct/',
    method: 'delete',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'api/yxVideoProduct',
    method: 'put',
    data
  })
}

export function delItems(ids) {
  return request({
    url: 'api/yxVideoProduct/',
    method: 'delete',
    data: ids
  })
}

export default { add, edit, del }
