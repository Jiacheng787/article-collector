<template>
  <div class="article">
    <van-search
      v-model="searchText"
      show-action
      placeholder="请输入搜索关键词"
      @focus="handleSearch"
    >
      <template #action>
        <van-icon name="filter-o" size="1rem" />
      </template>
    </van-search>
    <van-pull-refresh v-model="refreshing" @refresh="onRefreshAction">
      <van-list
        v-model="loading"
        :finished="finished"
        finished-text="没有更多了"
        @load="getArticleListAction"
      >
        <van-cell v-for="item in articleList" :key="item.id">
          <template #title>
            <article-card
              :id="item.id"
              :title="item.title"
              :thumb="item.thumb"
              :url="item.url"
              :tag="item.tag"
              :source="item.source"
              :collectedTime="item.collectedTime"
              :isFavorite="item.isFavorite"
              :isRead="item.isRead" />
          </template>
        </van-cell>
      </van-list>
    </van-pull-refresh>
  </div>
</template>

<script>
import { mapState, mapActions } from "vuex";
import { Search, List, Cell, PullRefresh, Icon } from "vant";
import ArticleCard from "@/components/ArticleCard";

export default {
  name: "article-collector-article",
  components: {
    [Search.name]: Search,
    [List.name]: List,
    [Cell.name]: Cell,
    [PullRefresh.name]: PullRefresh,
    [Icon.name]: Icon,
    ArticleCard,
  },
  data() {
    return {
      searchText: ""
    };
  },
  computed: {
    ...mapState(['articleList', 'finished']),
    loading: {
      get() {
        return this.$store.state.loading;
      },
      set(val) {
        this.$store.state.loading = val;
      }
    },
    refreshing: {
      get() {
        return this.$store.state.refreshing;
      },
      set(val) {
        this.$store.state.refreshing = val;
      }
    }
  },
  methods: {
    ...mapActions(["getArticleListAction", "onRefreshAction"]),
    handleSearch(e) {
      console.log(e);
    }
  },
};
</script>

<style lang="less" scoped>
</style>