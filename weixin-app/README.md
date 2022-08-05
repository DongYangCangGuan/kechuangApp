<!-- 代码提交 -->
提交代码前先查是否冲突
1. 先提交到自己的分支上：git checkout XXX (第一次新创用 git checkout -b XXX)；
2. git add .
3. git commit -m '备注'
4. git push (第一次提交到分支： git push -u origin Qiao)；/*提交到自己的分支 */
5. git checkout master /* 切换到master分支*/
6. git merge xxx /* 合并分支*/
7. git push
8. git checkout xxx /*切换回自己的分支进行开发 */

* git branch /*检查所在分支 */

<!-- 单位 -->


<!-- css -->
样式尽量按照html结构的顺序来写

<!-- 每次修改矢量图需要终端更新矢量图标 -->
npx iconfont-wechat

<!-- UI库  -->
已添加 weui

<!-- 埋点添加方法 -->
引入stopWatch.js中startTime, stopTime方法，分别表示进入页面时间和页面停留时长
页面onShow方法中添加进入方法的时间，调佣app.js中addIntoActionPoint方法记录进入页面埋点
onHide、onUnLoad方法中调用离开页面的埋点方法

<!-- source： 页面跳转来源，是从哪个页面跳转过来的，withParameters是携带的参数，reportId是进入报告页时的报告id -->
addIntoActionPoint: function(source,withParameters,reportId)

<!-- reportId: 报告页报告id，timeLong是在页面的停留时间 -->
addLeaveActionPoint(reportId,timeLong)    