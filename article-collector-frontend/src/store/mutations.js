const mutations = {
	setFavoriteStatus: (state, payload) => {
		const { id, isFavorite } = payload;
		console.log(id, isFavorite);
		state.articleList.find(item => item.id == id).isFavorite = isFavorite;
	},
	setDeleteArticle: (state, id) => {
		const index = state.articleList.findIndex(item => item.id == id);
		state.articleList.splice(index, 1);
	}
}

export default mutations;