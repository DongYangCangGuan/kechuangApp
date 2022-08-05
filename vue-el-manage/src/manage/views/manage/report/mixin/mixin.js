export default {
    data() {
        return {
            rules: {
                code: [{required: true, message: '请输入报告编号', trigger: 'blur'}],
                title: [{required: true, message: '请输入报告标题', trigger: 'blur'}],
                description: [{required: true, message: '请输入报告描述', trigger: 'blur'}],
                url: [{required: true, message: '请选择报告pdf文件', trigger: 'blur'}],
                // classifyValue: [{type: 'array', required: true, message: '请至少选择一个分类', trigger: 'change'}],
                // themeValue: [{type: 'array', required: true, message: '请至少选择一个主题', trigger: 'change'}],
                areaValue: [{type: 'array', required: true, message: '请至少选择一个地区', trigger: 'blur'}],
                industryValue: [{type: 'array', required: true, message: '请至少选择一个行业', trigger: 'blur'}],
                economyValue: [{type: 'array', required: true, message: '请至少选择一个宏观经济主题', trigger: 'blur'}],
                specialValue: [{type: 'array', required: true, message: '请至少选择一个专题报告主题', trigger: 'blur'}],
                labelValue: [{required: true, message: '请至少选择一个标签', trigger: 'change'}],
            },
            reportParam: {
                code: '',
                title: '',
                description: '',
                pic: '',
                url: '',
                classifyValue: [],
                labelValue: '',        //当前选中的标签列表
                themeValue: [],              //当前选中的报告专题
                industryValue: [],                   //当前选择行业
                areaValue: [],                      //当前所选报告区域
                economyValue: [],                      //当前选中的宏观经济主题
                specialValue: []                      //当前选中的专题报告主题
            },
            theme: [],                //所有报告专题列表项
            classify: [],           //分类列表
            industry: [],                    //全部行业列表
            reportLabel: [],                   //报告标签列表
            reportAreas: [],                     //报告区域列表
            economy: [],                          //宏观经济主题列表
            special: [],                             //专题报告主题列表
            loading: {
                form: false,
                btn: false
            }
        }
    },
    created() {
        this.getTheme();                //获取全部报告专题
        this.getIndustryList();            //获取全部行业
        this.getLabel();                    //获取所有标签
        this.getClassify();                 //获取所有分类列表
        this.getAreas();                //获取所有区域
        this.getEconomy();              //获取所有宏观经济主题
        this.getSpecial();              //获取所有专题报告主题
    },
    methods: {
        //获取所有报告专题
        getTheme() {
            this.$nextTick(async () => {
                this.theme = [];
                let params = {className: 'Dictionary', method: 'selectByKind', param: {kind: 'theme'}};
                let res = await this.$store.dispatch('http/post', params);
                for (let item of res) {
                    let obj = {
                        key: item.code,
                        label: item.name,
                        pic: item.pic,
                        value: false
                    }
                    this.theme.push(obj);
                }
            });
        },

        //获取所有行业列表信息
        getIndustryList() {
            this.$nextTick(async () => {
                this.theme = [];
                let params = {className: 'Dictionary', method: 'selectByKind', param: {kind: 'industry'}};
                let res = await this.$store.dispatch('http/post', params);
                for (let item of res) {
                    let obj = {
                        key: item.code,
                        label: item.name,
                        pic: item.pic,
                        value: false
                    }
                    this.industry.push(obj);
                }
            });


        },

        //获取所有标签信息
        getLabel() {
            this.$nextTick(async () => {
                this.theme = [];
                let params = {className: 'Dictionary', method: 'selectByKind', param: {kind: 'reportLabel'}};
                let res = await this.$store.dispatch('http/post', params);
                for (let item of res) {
                    let obj = {
                        key: item.code,
                        label: item.name,
                        value: false
                    }
                    this.reportLabel.push(obj);
                }
            });
        },

        //获取所有分类信息
        getClassify() {
            this.$nextTick(async () => {
                this.theme = [];
                let params = {className: 'Dictionary', method: 'selectByKind', param: {kind: 'reportkind'}};
                let res = await this.$store.dispatch('http/post', params);
                for (let item of res) {
                    let obj = {
                        key: item.code,
                        label: item.name,
                        value: false
                    }
                    this.classify.push(obj);
                }
            });

        },

        //获取所有区域
        getAreas() {
            this.$nextTick(async () => {
                this.theme = [];
                let params = {className: 'Dictionary', method: 'selectByKind', param: {kind: 'area'}};
                let res = await this.$store.dispatch('http/post', params);
                for (let item of res) {
                    let obj = {
                        key: item.code,
                        label: item.name,
                        value: false
                    }
                    this.reportAreas.push(obj);
                }
            });
        },

        //获取宏观经济主题
        getEconomy() {
            this.$nextTick(async () => {
                this.theme = [];
                let params = {className: 'Dictionary', method: 'selectByKind', param: {kind: 'economy'}};
                let res = await this.$store.dispatch('http/post', params);
                for (let item of res) {
                    let obj = {
                        key: item.code,
                        label: item.name,
                        pic: item.pic,
                        value: false
                    }
                    this.economy.push(obj);
                }
            });
        },

        //获取专题报告主题
        getSpecial() {
            this.$nextTick(async () => {
                this.theme = [];
                let params = {className: 'Dictionary', method: 'selectByKind', param: {kind: 'special'}};
                let res = await this.$store.dispatch('http/post', params);
                for (let item of res) {
                    let obj = {
                        key: item.code,
                        label: item.name,
                        pic: item.pic,
                        value: false
                    }
                    this.special.push(obj);
                }
            });
        },

        //移除上传图片
        OnPicRemove(file) {
            this.form.pic = "";
        },

        //覆盖默认的上传行为,图片上传
        picUpload(content) {
            this.$nextTick(async () => {
                this.loading.form = true;
                this.reportParam.pic = '';
                let fd = new FormData();
                fd.append('file', content.file);
                let params = {
                    param: fd,
                    url: process.env.BASE_API + '/api/file/imgFileUpload'
                };
                let response = await this.$store.dispatch('http/fileUpload', params);
                this.loading.form = false;
                if (response.code === 200) {
                    this.reportParam.pic = response.data;
                    this.$message({
                        message: '上传成功',
                        type: 'success'
                    });
                } else {
                    this.$message({
                        message: response.msg,
                        type: 'error'
                    });
                }
            });
        },

        //上传图片文件之前的钩子，参数为上传的文件，若返回 false 或者返回 Promise 且被 reject，则停止上传。
        beforePicUpload(file) {
            const extension1 = file.name.split('.')[file.name.split('.').length - 1] === 'jpg';
            const extension2 = file.name.split('.')[file.name.split('.').length - 1] === 'png';
            const extension3 = file.name.split('.')[file.name.split('.').length - 1] === 'gif';
            const isLt2M = file.size / 1024 / 1024 < 2;
            if (!extension1 && !extension2 && !extension3 && !extension4) {
                this.$message({
                    message: '上传模板只能是 jpg、png、gif格式!',
                    type: 'error',
                    duration: 2500
                });
                this.$refs.upload.clearFiles();
            }
            if (!isLt2M) {
                this.$message({
                    message: '上传模板大小不能超过 2MB!',
                    type: 'error',
                    duration: 2500
                });
                this.$refs.upload.clearFiles();
            }
            return (extension1 || extension2 || extension3) && isLt2M;
        },

        //上传pdf文件
        pdfUpload(content) {
            this.$nextTick(async () => {
                this.loading.form = true;
                let fd = new FormData();
                fd.append('file', content.file);
                let params = {
                    param: fd,
                    url: process.env.BASE_API + '/api/file/pdfFileUpload'
                };
                let response = await this.$store.dispatch('http/fileUpload', params);
                if (response.code === 200) {
                    this.reportParam.url = response.data;
                    this.$message({
                        message: '上传成功',
                        type: 'success'
                    });
                    this.fileList.push({name: this.reportParam.title, url: response.data});
                } else {
                    this.$message({
                        message: response.msg,
                        type: 'error'
                    });
                }
                this.loading.form = false;
            });
        },

        //上传pdf文件之前的钩子，参数为上传的文件，若返回 false 或者返回 Promise 且被 reject，则停止上传。
        beforePdfUpload(file) {
            this.fileList = [];
            const extension = file.name.split('.')[file.name.split('.').length - 1] === 'pdf';
            if (!extension) {
                this.$message({
                    message: '上传模板只能是 pdf格式!',
                    type: 'error',
                    duration: 2500
                });
                this.$refs.upload.clearFiles();
            }

            const isLt2M = file.size / 1024 / 1024 < 10;
            if (!isLt2M) {
                this.$message({
                    message: '上传模板大小不能超过 10MB!',
                    type: 'error',
                    duration: 2500
                });
                this.$refs.upload.clearFiles();
            }
            return extension && isLt2M;
        },

    }
}
