import request from '@/utils/request'
import qs from 'qs'

export function add(data) {
  return request({
    url: 'api/yxSystemStoreStaff',
    method: 'post',
    data
  })
}

export function del(ids) {
  return request({
    url: 'api/yxSystemStoreStaff/',
    method: 'delete',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'api/yxSystemStoreStaff',
    method: 'put',
    data
  })
}

export function find(params) {
  return request({
    url: 'api/yxSystemStoreStaff/find/' + params.uid + '/' + params.spreadUid,
    method: 'get'
  })
}

export default { add, edit, del, find }
