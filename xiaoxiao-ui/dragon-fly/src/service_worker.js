//service_worker.js
function download(url){
    var options={
        url:url
    }
    chrome.downloads.download(options)
}

//接收消息处理器
chrome.runtime.onMessage.addListener(function(message, sender,sendResponse) {
    if (message.type === 'down') {
        //调用下载方法
        download(message.data)
    }
});

async function getCurrentTab() {
    let queryOptions = { active: true, lastFocusedWindow: true };
    // `tab` will either be a `tabs.Tab` instance or `undefined`.
    let [tab] = await chrome.tabs.query(queryOptions);
    return tab;
}

chrome.action.onClicked.addListener(async (tab) => {
    getCurrentTab().then((tab) =>{
        var httpRequest = new XMLHttpRequest();//第一步：建立所需的对象
        httpRequest.open('GET', 'url', true);//第二步：打开连接  将请求参数写在url中  ps:"./Ptest.php?name=test&nameone=testone"
        httpRequest.send();//第三步：发送请求  将请求参数写在URL中
        /**
         * 获取数据后的处理程序
         */
        httpRequest.onreadystatechange = function () {
            if (httpRequest.readyState === 4 && httpRequest.status === 200) {
                chrome.notifications.create(
                    Math.random()+'',  // id
                    {
                        //type, iconUrl, title and message
                        type:"basic",
                        iconUrl: "icon128.png",
                        title: tab.title,
                        message: "收藏："+tab.url+"成功！",
                        contextMessage:tab.url
                    },
                    ()=>{
                        console.log(tab.url);
                    }
                );
            }
        };
    })

});