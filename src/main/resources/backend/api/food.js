// 查询列表接口
const getAccountPage = (params) => {
  return $axios({
    url: '/account/page',
    method: 'get',
    params
  })
}

// 删除接口
const deleteAccount = (ids) => {
  return $axios({
    url: '/account',
    method: 'delete',
    params: { ids }
  })
}

// 修改接口
const editAccount = (id,params) => {
  return $axios({
    url: `/account/${id}`,
    method: 'put',
    data: {...params }
  })
}

// 新增接口
const addAccount = (params) => {
  return $axios({
    url: '/account',
    method: 'post',
    data: { ...params }
  })
}

// 查询详情
const queryAccountById = (id) => {
  return $axios({
    url: `/account/${id}`,
    method: 'get'
  })
}



// 查菜品列表的接口
const queryAccountList = (params) => {
  return $axios({
    url: '/account/list',
    method: 'get',
    params
  })
}



// 起售停售---批量起售停售接口
const AccountStatusByStatus = (params) => {
  return $axios({
    url: `/account/status/${params.status}`,
    method: 'post',
    params: { ids: params.id }
  })
}