import request from '@/utils/request'

export function add(data) {
  return request({
    url: 'api/yxVideo',
    method: 'post',
    data
  })
}

export function del(ids) {
  return request({
    url: 'api/yxVideo/',
    method: 'delete',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'api/yxVideo',
    method: 'put',
    data
  })
}

export function addItems(data) {
  return request({
    url: 'api/yxVideo/addVideoItems',
    method: 'post',
    data
  })
}

export function updateItems(data) {
  return request({
    url: 'api/yxVideo/editVideoItems',
    method: 'post',
    data
  })
}

export default { add, edit, del }
