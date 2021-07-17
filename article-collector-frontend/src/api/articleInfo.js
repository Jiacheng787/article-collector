import request from '@/utils/request';

const baseURL = '/api/article-collector/v1/articleInfo';
const articleInfo = {};

articleInfo.getArticleList = function(current="", size="") {
    return request({
        url: `${baseURL}?current=${current}&size=${size}`,
        method: 'get'
    })
}

/**
 * 从剪贴板读取 url ，爬取文章内容并添加到数据库
 * @param {Object} param 包含 url 和标签的对象
 * @returns affected rows
 */
articleInfo.addArticle = function(param) {
    return request({
        url: `${baseURL}`,
        data: param,
        method: 'post'
    })
}

/**
 * 添加或删除星标
 * @param {String} articleId 文章 ID
 * @param {Number} isFavorite 需要更新的星标状态
 * @returns affected rows
 */
articleInfo.changeFavoriteStatus = function(articleId="", isFavorite=0) {
    return request({
        url: `${baseURL}?articleId=${articleId}&isFavorite=${isFavorite}`,
        method: 'put'
    })
}

/**
 * 删除文章
 * @param {String} articleId 文章 ID
 * @returns 
 */
articleInfo.deleteArticle = function(articleId="") {
    return request({
        url: `${baseURL}?articleId=${articleId}`,
        method: 'delete'
    })
}

export default articleInfo;