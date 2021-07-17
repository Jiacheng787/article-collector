<template>
  <div class="home">
      <van-cell-group v-for="(card, cardIndex) in weeklyOverview" :key="cardIndex" :title="card.title">
        <template v-if="card.articles.length">
          <van-cell v-for="(article, articleIndex) in card.articles" :key="articleIndex">
            <template #title>
              <div class="weekly-card">
                <van-image
                  class="thumb"
                  width="3rem"
                  height="3rem"
                  :src="article.thumb"
                />
                <div class="article-info">
                  <div class="header">
                    <div class="title">{{ article.title }}</div>
                    <van-tag class="read-switch-tag" v-if="article.isRead" plain type="success">已读</van-tag>
                    <van-tag class="read-switch-tag" v-else plain type="danger">未读</van-tag>
                  </div>
                  <div class="footer">
                    <div class="tag-list">
                      <van-tag round type="primary">标签</van-tag>
                    </div>
                    <div class="source">{{ article.source }}</div>
                  </div>
                </div>
              </div>
            </template>
          </van-cell>
        </template>
        <template v-else>
          <van-cell>
            <template #title>
              <div class="empty-state">
                暂时还没有添加文章哦
              </div>
            </template>
          </van-cell>
        </template>
      </van-cell-group>
  </div>
</template>

<script>
import { Cell as VanCell, CellGroup as VanCellGroup, Image as VanImage, Tag as VanTag, Toast } from "vant";

export default {
  name: "article-collector-home",
  components: {
    VanCell,
    VanCellGroup,
    VanImage,
    VanTag
  },
  data() {
    return {
      weeklyOverview: [
        {
          title: "今天",
          articles: [
            {
              title: "文章标题1",
              thumb: "缩略图",
              url: "文章链接",
              isRead: false,
              tag: ["字节", "前端面试"],
              source: "前端印象"
            },
            {
              title: "文章标题2",
              thumb: "缩略图",
              url: "文章链接",
              isRead: true,
              tag: ["字节", "前端面试"],
              source: "前端印象"
            },
            {
              title: "文章标题2",
              thumb: "缩略图",
              url: "文章链接",
              isRead: true,
              tag: ["字节", "前端面试"],
              source: "前端印象"
            },
          ],
        },
        {
          title: "昨天",
          articles: [
            {
              title: "文章标题3",
              thumb: "缩略图",
              url: "文章链接",
              isRead: true,
              tag: ["字节", "前端面试"],
              source: "前端印象"
            },
            {
              title: "文章标题4",
              thumb: "缩略图",
              url: "文章链接",
              isRead: true,
              tag: ["字节", "前端面试"],
              source: "前端印象"
            },
          ],
        },
        {
          title: "2021/06/24",
          articles: [
            {
              title: "文章标题1",
              thumb: "缩略图",
              url: "文章链接",
              isRead: false,
              tag: ["字节", "前端面试"],
              source: "前端印象"
            },
            {
              title: "文章标题2",
              thumb: "缩略图",
              url: "文章链接",
              isRead: true,
              tag: ["字节", "前端面试"],
              source: "前端印象"
            },
          ],
        },
      ],
    };
  },
  methods: {},
};
</script>

<style lang="less" scoped>
::v-deep .van-cell-group__title {
  color: #323233;
  font-size: 1.2rem;
  font-weight: bold;
}
::v-deep .van-cell-group {
  margin: 0 12px;
  overflow: hidden;
  border-radius: 8px;
}
::v-deep .van-cell {
  padding: 10px;
  &::after {
    right: 10px;
    left: 10px;
  }
}
::v-deep .van-tag.van-tag--success.van-tag--plain {
  color: #07c16099;
}
.home {
  box-sizing: border-box; // 不加的话一开始没有内容的时候会出现滚动条
  min-height: calc(100vh - 96px); // 设置最小高度，确保背景色在没有内容的时候也可以充满容器
  background-color: #f7f8fa;
  padding-bottom: 66px; // 容器底部设置内边距，防止内容被底部 tab 栏遮挡
  .weekly-card {
    display: flex;
    .thumb {
      flex-shrink: 0;
      overflow: hidden; // 需要设置溢出隐藏，否则图片外面的 div 是透明的，border-radius 设置看不到效果
      border-radius: 4px;
    }
    .article-info {
      // flex 布局的项目里面继续用 flex 布局，设置 space-between 不生效通常是项目宽度没有设置，或者浏览器计算布局的时候无法检测到宽度
      // 两种方案，一个是写死宽度，width: 260px
      // 另一个是设置 flex-grow: 1，让项目自动填充剩余空间
      flex-shrink: 0;
      flex-grow: 1;
      margin-left: 12px;
      .header {
        display: flex;
        justify-content: space-between;
        .title {
          width: 15rem;
          overflow: hidden;
          white-space: nowrap;
          text-overflow: ellipsis;
        }
        .read-switch-tag {
          margin-top: 2px;
          padding: 2px;
          height: 14px;
        }
      }
      .footer {
        display: flex;
        justify-content: space-between;
        .source {
          color: rgba(69, 90, 100, 0.6);
        }
      }
    }
  }
  .empty-state {
    text-align: center;
    color: rgba(69, 90, 100, 0.6);
  }
}
</style>