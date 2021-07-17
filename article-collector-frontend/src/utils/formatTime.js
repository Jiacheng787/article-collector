// import moment from "moment";

function formatTime(timestamp) {
    // return moment(timestamp).format("YYYY/MM/DD");
    // 使用 24 小时制，输出 "19:00:00"
    // return new Date(timestamp).toLocaleTimeString("zh-CN", { hour12: false });
    return new Date(timestamp).toLocaleDateString("zh-CN");
}

export default formatTime;