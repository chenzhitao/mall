import request from '@/utils/request'

export function add(data) {
  return request({
    url: 'api/yxVideoType',
    method: 'post',
    data
  })
}

export function del(ids) {
  return request({
    url: 'api/yxVideoType/',
    method: 'delete',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'api/yxVideoType',
    method: 'put',
    data
  })
}

export function queryAllVideoTypes(data) {
  return request({
    url: 'api/yxVideoType/getAllVideoTypes',
    method: 'get',
    params:data
  })
}

export default { add, edit, del }
