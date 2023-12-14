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
        // 使用 fetch 发送 GET 请求
        fetch('http://localhost:17700/article/reptile?url='+tab.url)
            .then(response => {
                // 确保请求成功
                if (response.ok) {
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
                // 处理响应数据，返回一个 Promise 对象
                return response.text();
            })
            .then(data => {
                // 处理响应数据
                console.log(data);
            })
            .catch(error => {
                // 处理错误
                console.error("发生错误：", error);
            });
    })

});