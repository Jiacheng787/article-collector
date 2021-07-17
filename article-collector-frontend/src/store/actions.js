import api from "@/api";
import { Toast } from "vant";

const actions = {
	/**
	 * 懒加载分页获取文章列表
	 * @param {Object} context 
	 */
	getArticleListAction: ({ commit, state }) => {
		if (state.refreshing) {
			state.refreshing = false;
			state.current = 1; // 下拉刷新，当前页数回到第一页
			state.articleList = [];
		}

		api.articleInfo.getArticleList(state.current, state.size)
			.then(res => {
				state.articleList = state.articleList.concat(res.records);
				state.total = res.total;
				state.current++; // 懒加载，每次加载之后 current + 1，为下一页加载准备

				// 数据全部加载完成
				if (Math.ceil(state.total / state.size) < state.current) {
					state.finished = true;
				}
			})
			.catch(e => {
				Toast(e.message);
			})
			.finally(() => {
				// 加载状态结束
				state.loading = false;
			})
	},
	/**
	 * 下拉刷新操作
	 * @param {*} context 
	 */
	onRefreshAction: ({ dispatch, state }) => {
		// 清空列表数据
		state.finished = false;

		// 重新加载数据
		// 将 loading 设置为 true，表示处于加载状态
		state.loading = true;
		// refreshing 本来是 v-model 绑定的，只有下拉刷新才会变成 true
		// 这样的话，在其他组建就不能调用这个方法了
		// 这边强行将这个字段设为 true ，以便在其他组建调用
		state.refreshing = true;
		dispatch("getArticleListAction");
	},
	/**
	 * 星标操作
	 * @param {*} context 
	 */
	changeFavoriteAction: ({ commit, state }, payload) => {
		const { id, isFavorite } = payload;
		api.articleInfo.changeFavoriteStatus(id, isFavorite)
			.then(() => {
				commit("setFavoriteStatus", { id, isFavorite })
			})
			.catch(e => {
				Toast(e.message);
			})
	},
	/**
	 * 删除文章操作
	 * @param {*} context 
	 * @param {*} id 
	 */
	deleteArticleAction: ({ commit, state }, id) => {
		api.articleInfo.deleteArticle(id)
			.then(() => {
				commit("setDeleteArticle", id);
			})
			.catch(e => {
				Toast(e.message);
			})
	}
}

export default actions;