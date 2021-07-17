<template>
  <van-swipe-cell>
    <template #left>
      <van-button square type="primary" text="星标" @click="handleFlavorite" />
      <van-button square type="info" text="添加" />
    </template>
    <div class="article-card">
      <!-- <van-image
        ref="thumbnail"
        class="thumbnail"
        width="4rem"
        height="4rem"
        :lazy-load="true"
        :src="thumb"
      /> -->
      <img class="thumbnail" :src="thumb" style="width: 4rem; height: 4rem;" :lazy-load="true" referrerpolicy="no-referrer" />
      <div class="right">
        <div class="header">
          <div :class="`title ${isFavorite ? 'favorite' : ''}`">
            {{ title }}
          </div>
          <van-tag class="read-switch-tag" v-if="isRead" plain type="success">已读</van-tag>
          <van-tag class="read-switch-tag" v-else plain type="danger">未读</van-tag>
        </div>
        <div class="footer">
          <div class="tag-list">
            <van-tag
              v-for="(item, index) in tagList"
              :key="index"
              style="margin-right: 4px"
              round
              type="primary"
            >
              {{ item }}
            </van-tag>
          </div>
          <div class="time">{{ source }} {{ date(collectedTime) }}</div>
        </div>
      </div>
    </div>
    <template #right>
      <van-button square text="删除" type="danger" class="delete-button" @click="handleDelete" />
    </template>
  </van-swipe-cell>
</template>

<script>
import { mapActions } from "vuex";
import formatTime from "@/utils/formatTime";
import {
  Tag as VanTag,
  SwipeCell as VanSwipeCell,
	Button as VanButton,
} from "vant";

export default {
  name: "article-card",
  components: {
    VanTag,
    VanSwipeCell,
		VanButton
  },
  props: {
    id: {
      type: String,
      default: "",
    },
    title: {
      type: String,
      default: "文章标题文章标题文章标题文章标题文章标题文章标题",
    },
    thumb: {
      type: String,
      default: "https://img01.yzcdn.cn/vant/ipad.jpeg",
    },
    url: {
      type: String,
      default: ""
    },
    tag: {
      type: String,
      default: "JS 前端面试 ES6"
    },
    source: {
      type: String,
      default: "微信公众号"
    },
    collectedTime: {
      type: Number,
      default: 0,
    },
    isFavorite: {
      type: Number,
      default: 0,
    },
    isRead: {
      type: Number,
      default: 0,
    },
  },
  data() {
    return {};
  },
  computed: {
    tagList() {
      return this.tag.split(" ");
    },
    date() {
      return (timestamp) => {
        return formatTime(timestamp);
      }
    }
  },
  methods: {
    ...mapActions(["changeFavoriteAction", "deleteArticleAction"]),
    handleFlavorite() {
      const isFavorite = this.isFavorite == 1 ? 0 : 1;
      this.changeFavoriteAction({id: this.id, isFavorite});
    },
    handleDelete() {
      this.deleteArticleAction(this.id);
    }
  },
};
</script>

<style lang="less" scoped>
::v-deep .van-button {
  height: 100%;
}
::v-deep .van-tag.van-tag--success.van-tag--plain {
  color: #07c16099;
}
.article-card {
  height: 4rem;
  display: flex;
  .thumbnail {
    flex-shrink: 0;
  }
  .right {
    flex-grow: 1;
    margin-left: 10px;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    .header {
      display: flex;
      justify-content: space-between;
      .title {
        // flex-grow: 1;
        width: 15rem;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
      }
      .title.favorite {
        color: #07c160;
        font-weight: bold;
        width: 15rem;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
      }
      .read-switch-tag {
        // flex-shrink: 0;
        margin-top: 2px;
        padding: 2px;
        height: 14px;
      }
    }
    .footer {
      display: flex;
      justify-content: space-between;
      .time {
        color: #969799;
      }
    }
  }
}
</style>