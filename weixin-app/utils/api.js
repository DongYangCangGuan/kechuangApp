const {
	Encrypt,
	Decrypt
} = require("./encrypt");
const app = getApp();
// 接口列表
const apiList = [{
		path: "../pages/mine/mock", // mock.js文件的位置（建在该文件夹下）
		url: "getLoadingInfo", // 接口方法名
		mock: "getLoadingInfo" // mock方法名
	},
	{
		path: "../pages/index/mock",
		url: "getLoadingData", // 接口方法名
		mock: "getLoadingData" // mock方法名
	},
	//获取通知新闻
	{
		path: "../pages/index/mock",
		url: "getInform", // 接口方法名
		mock: "getInform" // mock方法名
	},
	//获取审批
	{
		path: "../pages/mine/member/mock",
		url: "getApproveList", // 接口方法名
		mock: "getApproveList" // mock方法名
	},
	//获取审批详情
	{
		path: "../pages/mine/memberDetail/mock",
		url: "getMenberDetail", // 接口方法名
		mock: "getMenberDetail" // mock方法名
	},
//审批
{
	path: "../pages/mine/member/mock",
	url: "approve", // 接口方法名
	mock: "approve" // mock方法名
},
//获取消息
{
	path: "../pages/mine/myMessage/mock",
	url: "getNotes", // 接口方法名
	mock: "getNotes" // mock方法名
},
//获取活动
{
	path: "../pages/mine/feedbackItems/mock",
	url: "getSignDetail", // 接口方法名
	mock: "getSignDetail" // mock方法名
},
//获取活动详情
{
	path: "../pages/mine/feedBack/mock",
	url: "getSignList", // 接口方法名
	mock: "getSignList" // mock方法名
},
//更新消息读取状态
{
	path: "../pages/mine/feedBack/mock",
	url: "updateStatus", // 接口方法名
	mock: "updateStatus" // mock方法名
},
//更新问卷调查
{
	path: "../pages/mine/questionnaire/mock",
	url: "getAnswerList", // 接口方法名
	mock: "getAnswerList" // mock方法名
},
//更新问答详情
{
	path: "../pages/mine/questionnaireres/mock",
	url: "getAnswerDetail", // 接口方法名
	mock: "getAnswerDetail" // mock方法名
},
//获取收藏信息
{
	path: "../pages/mine/collect/mock",
	url: "getCollect", // 接口方法名
	mock: "getCollect" // mock方法名
},
	//获取公司名称和编号
	{
		path: "../pages/mine/authentification/mock",
		url: "checkMember", // 接口方法名
		mock: "checkMember" // mock方法名
	},
		//获取公司名称和编号
		{
			path: "../pages/mine/authentification/mock",
			url: "getMembers", // 接口方法名
			mock: "getMembers" // mock方法名
		},
	// 首页展示的信息
	{
		path: "../pages/index/mock",
		url: "getKindInfo", // 接口方法名
		mock: "getKindInfo" // mock方法名
	},
	//获取消息
	{
		path: "../pages/index/advices/mock",
		url: "getAdvices", // 接口方法名
		mock: "getAdvices" // mock方法名
	},
	//获取足迹
	{
		path: "../pages/mine/history/mock",
		url: "getPoint", // 接口方法名
		mock: "getPoint" // mock方法名
	},
	

	// 研报
	{
		path: "../pages/report/mock",
		url: "getMainInfo", // 接口方法名
		mock: "getMainInfo" // mock方法名
	},
	//展示所有资讯报告信息
	{
		path: "../pages/index/infoReport/mock",
		url: "getAllInfoReport", // 接口方法名
		mock: "getAllInfoReport" // mock方法名
	},

	{
		path: "../pages/custom/mock",
		url: "getLoading", // 接口方法名
		mock: "getLoading" // mock方法名
	},
	{
		path: "../pages/custom/mock",
		url: "getCustom", // 接口方法名
		mock: "getCustom" // mock方法名
	},
	{
		path: "../pages/custom/details/mock",
		url: "getCustomDetails", // 接口方法名
		mock: "getCustomDetails" // mock方法名
	},
	{
		path: "../pages/custom/details/mock",
		url: "getAdvisory", // 接口方法名
		mock: "getAdvisory" // mock方法名
	}, {
		path: "../pages/live/mock",
		url: "getLiveData", // 接口方法名
		mock: "getLiveData" // mock方法名
	}, {
		path: "../pages/mine/history/mock",
		url: "getHistoryData", // 接口方法名
		mock: "getHistoryData" // mock方法名
	}, {
		path: "../pages/mine/feedback/mock",
		url: "getBackData", // 接口方法名
		mock: "getBackData" // mock方法名
	},
	{
		path: "../pages/product/entfirm/loans/nextpage/mock",
		url: "getListData", // 接口方法名
		mock: "getListData" // mock方法名
	}
]

//封装http 请求方法(包含加密解密)
// const http = (params, resolve, reject) => {
// 	wx.showLoading({
// 		title: "加载中",
// 		mask: true,
// 	});
// 	let data = {
// 		className: params.className,
// 		method: params.method,
// 		param: params.data
// 	}
// 	wx.request({
// 		url: app.globalData.apiUrl,
// 		data: {
// 			param: Encrypt(data)
// 		},
// 		header: {
// 			"Content-Type": "application/json",
// 			"token": wx.getStorageSync('token') || '',
// 		},
// 		method: 'POST',
// 		success: function (res) {
// 			if (res != null && res.data != null && res.data != '') {
// 				var decryptParam = Decrypt(res.data);
// 				if (typeof decryptParam != 'object') {
// 					decryptParam = decryptParam.replace(/\ufeff/g, ""); //重点
// 					if (decryptParam != null && decryptParam != '')
// 						res.data = JSON.parse(decryptParam);
// 				}
// 				resolve(res);
// 			}
// 			reject('err');
// 		},
// 		fail: function (e) {
// 			reject(e)
// 		},
// 		complete: res => {
// 			wx.hideLoading();
// 		}
// 	})
// }

// 封装http 请求方法(不包含加密解密)
const http = (params, resolve, reject) => {
	wx.showLoading({
		title: "加载中",
		mask: true,
	});
	let data = {
		className: params.className,
		method: params.method,
		param: JSON.stringify(params.data)
	}
	wx.request({
		url: app.globalData.apiUrl,
		data: data,
		header: {
			"Content-Type": "application/json",
			"token": wx.getStorageSync('token') || '',
		},
		method: 'POST',
		success: function (res) {
			resolve(res)
		},
		fail: function (e) {
			reject(e)
		},
		complete: res => {
			wx.hideLoading();
		}
	})
}

// mock
const http2 = (params, resolve, reject) => {
	console.log("输入参数为", params)
	for (let item of apiList) {
		// console.log(item.url, params.url)
		if (item.url === params.url) {
			const path = require(item.path);
			resolve(path[item.mock]);
		}
	}
	reject({
		errMsg: "URL未定义"
	});
}

export const post = (params) => {
	return new Promise((resolve, reject) => {
		app.globalData.mock ? http2(params, resolve, reject) : http(params, resolve, reject);
	})
}

//文件上传
export const fileUpload = (params) => {
	return new Promise((resolve, reject) => {
		wx.request({
			url: app.globalData.fileUrl + params.url,
			data: params.data,
			header: {
				"Content-Type": "application/json",
				"token": wx.getStorageSync('token') || '',
			},
			method: 'POST',
			success: function (res) {
        var decryptParam = Decrypt(res.data);
				if (typeof decryptParam != 'object') {
					decryptParam = decryptParam.replace(/\ufeff/g, ""); //重点
					if (decryptParam != null && decryptParam != '')
						res.data = JSON.parse(decryptParam);
				}
				if (res != null && res.data != null && res.data != '') {
					resolve(res);
				}
				reject('err');
			},
			fail: function (e) {
				reject(e)
			}
		});
	});
}

//文件下载
export const fileDownload = (params) => {
	return new Promise((resolve, reject) => {
		let data = {
			filePath: params.data
		};
		wx.request({
			url: app.globalData.fileUrl + params.url,
			data: Encrypt(data),
			header: {
				"Content-Type": "application/json",
				"token": wx.getStorageSync('token') || '',
			},
			method: 'POST',
			success: function (res) {
				var decryptParam = Decrypt(res.data);
				if (typeof decryptParam != 'object') {
					decryptParam = decryptParam.replace(/\ufeff/g, ""); //重点
					if (decryptParam != null && decryptParam != '')
						res.data = JSON.parse(decryptParam);
				}
				if (res != null && res.data != null && res.data != '') {
					resolve(res);
				}
				reject('err');
			},
			fail: function (e) {
				reject(e)
			}
		});
	});
}