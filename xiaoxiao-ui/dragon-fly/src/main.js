/**
 * 显示网络图片的内存大小
 * @param {*} src
 * @returns
 */
function getByte(src){
    return fetch(src).then(function(res){
        return res.blob()
    }).then(function(data){
        return (data.size/(1024)).toFixed(2)+'kB'
    })
}

/**
 * 基于dom的title属性来设置显示图片信息
 * @param {*} el
 * @param {number} byte zijie
 */
function showInfo(el,byte){
    el.title=`真实尺寸:${el.naturalWidth}*${el.naturalHeight}\n显示尺寸:${el.width}*${el.height}\n存储大小:${byte}`
}

/**
 * 在document上代理mouseover事件
 */
document.addEventListener('mouseover',function(e){
    //移动到图片元素上时、则显示信息
    if(e.target.tagName==='IMG'){
        getByte(e.target.src).then(byte=>{
            showInfo(e.target,byte)
        })
    }
},true)

/**
 * 在document上代理mouseover事件
 */
document.addEventListener('dragend',async function (e) {
    if (e.target.tagName === 'IMG') {
        //发生消息，从content_scripts发送到扩展页面
        await chrome.runtime.sendMessage({type: 'down', data: e.target.src});
    }
})