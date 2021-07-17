const state = {
    articleList: [],
    current: 1, // 当前页数，默认先展示第一页
    total: 0, // 总记录条数
    size: 15, // 每页展示条数，如果太小会导致页面一次加载两页的数据
    loading: false,
    finished: false,
    refreshing: false
}

export default state;