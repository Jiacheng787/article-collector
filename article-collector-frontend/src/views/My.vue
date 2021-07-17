<template>
  <div class="my">
		<!-- 用户资料卡片 -->
    <van-cell class="profile-card">
      <template #title>
        <div class="profile-container">
          <van-image round fit="cover" width="4rem" height="4rem" />
          <div class="profile">
            <div class="username">Garfield</div>
            <div class="signature">个性签名</div>
          </div>
        </div>
      </template>
    </van-cell>
		<!-- 信息总览卡片 -->
    <van-grid :column-num="2" class="overviews">
      <van-grid-item>
        <div class="category">文章总数</div>
        <div class="count-num">{{ 2333 }}</div>
      </van-grid-item>
      <van-grid-item>
        <div class="category">已读文章</div>
        <div class="count-num">{{ 133 }}</div>
      </van-grid-item>
      <van-grid-item>
        <div class="category">创建笔记</div>
        <div class="count-num">--</div>
      </van-grid-item>
      <van-grid-item>
        <div class="category">创建列表</div>
        <div class="count-num">--</div>
      </van-grid-item>
    </van-grid>
		<!-- 操作菜单卡片 -->
    <van-cell-group class="menu-group">
      <van-cell title="自定义标签" is-link @click="handleCustomTag" />
      <van-cell title="自定义来源" is-link @click="handleCustomSource" />
    </van-cell-group>
		<van-action-sheet v-model="show" :title="actionSheetContent.title">
			<div class="action-sheet">{{ actionSheetContent.content }}</div>
		</van-action-sheet>
  </div>
</template>

<script>
import {
  Grid as VanGrid,
  GridItem as VanGridItem,
  Cell as VanCell,
  CellGroup as VanCellGroup,
  Image as VanImage,
	ActionSheet as VanActionSheet,
  Toast,
} from "vant";

export default {
  name: "article-collector-my",
  components: {
    VanGrid,
    VanGridItem,
    VanCell,
    VanCellGroup,
    VanImage,
		VanActionSheet
  },
  data() {
    return {
			show: false,
			actionSheetContent: {
				title: "",
				content: ""
			}
		};
  },
  methods: {
    handleCustomTag() {
			this.actionSheetContent = Object.assign({}, {
				title: "自定义标签",
				content: "自定义标签"
			})
      this.show = true;
    },
    handleCustomSource() {
			this.actionSheetContent = Object.assign({}, {
				title: "自定义来源",
				content: "自定义来源"
			})
      this.show = true;
    },
  },
};
</script>

<style lang="less" scoped>
.my {
  height: calc(100vh - 96px);
  background-color: #f7f8fa;
	padding-top: 24px;
	.profile-container {
		display: flex;
		align-items: center;
		.profile {
			margin-left: 12px;
			.username {
				font-size: 1.5rem;
			}
			.signature {
				color: rgba(69, 90, 100, 0.6);
			}
		}
	}
	.overviews {
		margin-top: 24px;
		.category {
			color: rgba(69, 90, 100, 0.6);
		}
		.count-num {
			font-size: 3rem;
		}
	}
  .menu-group {
    margin-top: 24px;
  }
	.action-sheet {
		height: 70vh;
		padding: 12px;
	}
}
</style>