<template>
  <van-popup
    class="add-popup"
    v-model="isAddPopShow"
    round
    closeable
    position="bottom"
    :style="{ height: '35%' }"
  >
    <van-form class="add-article-form" @submit="onSubmit">
      <van-field
        ref="article-url"
        v-model="form.url"
        name="url"
        label="链接"
        placeholder="复制链接到此处"
      />
      <van-field
        v-model="form.tag"
        name="tag"
        label="标签"
        placeholder="标签以空格分隔，不超过三个"
      />
      <van-field
        v-model="form.source"
        name="source"
        label="来源"
        placeholder="自定义文章来源"
      />
      <div style="margin: 16px">
        <van-button
          round
          block
          type="info"
          native-type="submit"
          :loading="isLoading"
          loading-text="加载中..."
        >
          提交
        </van-button>
      </div>
    </van-form>
  </van-popup>
</template>

<script>
import { mapActions } from "vuex";
import api from "@/api";
import { Popup, Form, Field, Button, Toast } from "vant";

export default {
  name: "add-popup",
  components: {
    [Popup.name]: Popup,
    [Form.name]: Form,
    [Field.name]: Field,
		[Button.name]: Button
  },
	watch: {
		isAddPopShow: function handler(next, prev) {
			if (!next) {
        // 弹框关闭清空表单
				this.$nextTick(() => {
					this.form = Object.assign({}, {
						url: "",
						tag: "",
            source: ""
					})
				})
			} else {
        // 弹框打开从剪贴板读取内容
        this.onPaste();
      }
		}
	},
  data() {
    return {
      isAddPopShow: false,
      isLoading: false,
			form: {
				url: "",
				tag: "",
        source: ""
			}
    };
  },
	methods: {
    ...mapActions(["onRefreshAction"]),
    async onPaste() {
      if (!window.navigator.clipboard) {
        Toast.fail('当前环境不支持剪贴板！');
        return;
      }
      try {
        let text = await window.navigator.clipboard.readText();
        this.form.url = text;
      } catch (error) {
        Toast.fail('剪贴板读取失败！');
      }
    },
		onSubmit(values) {
      console.log('submit', values);
      this.isLoading = true;
      api.articleInfo.addArticle(values)
        .then(() => {
          Toast.success("添加成功");
          this.isAddPopShow = false;
          this.onRefreshAction();
        })
        .catch(e => {
          Toast(e.message);
        })
        .finally(() => {
          this.isLoading = false;
        })
    },
	}
};
</script>

<style lang="less" scoped>
::v-deep .van-cell__title.van-field__label {
	width: 3.2rem;
}
.add-popup {
  box-sizing: border-box;
  padding: 10px;
	padding-top: 50px;
}
</style>