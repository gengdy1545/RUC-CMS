(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-ed0e42c8"],{"202d":function(e,t,n){"use strict";n.r(t);var i=function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("div",{staticClass:"app-container"},[n("el-form",{directives:[{name:"show",rawName:"v-show",value:e.showSearch,expression:"showSearch"}],ref:"queryForm",attrs:{model:e.queryParams,inline:!0,"label-width":"68px"}},[n("el-form-item",{attrs:{label:"公告标题",prop:"noticeTitle"}},[n("el-input",{attrs:{placeholder:"请输入公告标题",clearable:"",size:"small"},nativeOn:{keyup:function(t){return!t.type.indexOf("key")&&e._k(t.keyCode,"enter",13,t.key,"Enter")?null:e.handleQuery(t)}},model:{value:e.queryParams.noticeTitle,callback:function(t){e.$set(e.queryParams,"noticeTitle",t)},expression:"queryParams.noticeTitle"}})],1),n("el-form-item",{attrs:{label:"操作人员",prop:"createBy"}},[n("el-input",{attrs:{placeholder:"请输入操作人员",clearable:"",size:"small"},nativeOn:{keyup:function(t){return!t.type.indexOf("key")&&e._k(t.keyCode,"enter",13,t.key,"Enter")?null:e.handleQuery(t)}},model:{value:e.queryParams.createBy,callback:function(t){e.$set(e.queryParams,"createBy",t)},expression:"queryParams.createBy"}})],1),n("el-form-item",{attrs:{label:"类型",prop:"noticeType"}},[n("el-select",{attrs:{placeholder:"公告类型",clearable:"",size:"small"},model:{value:e.queryParams.noticeType,callback:function(t){e.$set(e.queryParams,"noticeType",t)},expression:"queryParams.noticeType"}},e._l(e.dict.type.sys_notice_type,(function(e){return n("el-option",{key:e.value,attrs:{label:e.label,value:e.value}})})),1)],1),n("el-form-item",[n("el-button",{attrs:{type:"primary",icon:"el-icon-search",size:"mini"},on:{click:e.handleQuery}},[e._v("搜索")]),n("el-button",{attrs:{icon:"el-icon-refresh",size:"mini"},on:{click:e.resetQuery}},[e._v("重置")])],1)],1),n("el-row",{staticClass:"mb8",attrs:{gutter:10}},[n("el-col",{attrs:{span:1.5}},[n("el-button",{directives:[{name:"hasPermi",rawName:"v-hasPermi",value:["system:notice:add"],expression:"['system:notice:add']"}],attrs:{type:"primary",plain:"",icon:"el-icon-plus",size:"mini"},on:{click:e.handleAdd}},[e._v("新增")])],1),n("el-col",{attrs:{span:1.5}},[n("el-button",{directives:[{name:"hasPermi",rawName:"v-hasPermi",value:["system:notice:edit"],expression:"['system:notice:edit']"}],attrs:{type:"success",plain:"",icon:"el-icon-edit",size:"mini",disabled:e.single},on:{click:e.handleUpdate}},[e._v("修改")])],1),n("el-col",{attrs:{span:1.5}},[n("el-button",{directives:[{name:"hasPermi",rawName:"v-hasPermi",value:["system:notice:remove"],expression:"['system:notice:remove']"}],attrs:{type:"danger",plain:"",icon:"el-icon-delete",size:"mini",disabled:e.multiple},on:{click:e.handleDelete}},[e._v("删除")])],1),n("right-toolbar",{attrs:{showSearch:e.showSearch},on:{"update:showSearch":function(t){e.showSearch=t},"update:show-search":function(t){e.showSearch=t},queryTable:e.getList}})],1),n("el-table",{directives:[{name:"loading",rawName:"v-loading",value:e.loading,expression:"loading"}],attrs:{data:e.noticeList},on:{"selection-change":e.handleSelectionChange}},[n("el-table-column",{attrs:{type:"selection",width:"55",align:"center"}}),n("el-table-column",{attrs:{label:"序号",align:"center",prop:"noticeId",width:"100"}}),n("el-table-column",{attrs:{label:"公告标题",align:"center",prop:"noticeTitle","show-overflow-tooltip":!0}}),n("el-table-column",{attrs:{label:"公告类型",align:"center",prop:"noticeType",width:"100"},scopedSlots:e._u([{key:"default",fn:function(t){return[n("dict-tag",{attrs:{options:e.dict.type.sys_notice_type,value:t.row.noticeType}})]}}])}),n("el-table-column",{attrs:{label:"状态",align:"center",prop:"status",width:"100"},scopedSlots:e._u([{key:"default",fn:function(t){return[n("dict-tag",{attrs:{options:e.dict.type.sys_notice_status,value:t.row.status}})]}}])}),n("el-table-column",{attrs:{label:"创建者",align:"center",prop:"createBy",width:"100"}}),n("el-table-column",{attrs:{label:"创建时间",align:"center",prop:"createTime",width:"100"},scopedSlots:e._u([{key:"default",fn:function(t){return[n("span",[e._v(e._s(e.parseTime(t.row.createTime,"{y}-{m}-{d}")))])]}}])}),n("el-table-column",{attrs:{label:"操作",align:"center","class-name":"small-padding fixed-width"},scopedSlots:e._u([{key:"default",fn:function(t){return[n("el-button",{directives:[{name:"hasPermi",rawName:"v-hasPermi",value:["system:notice:edit"],expression:"['system:notice:edit']"}],attrs:{size:"mini",type:"text",icon:"el-icon-edit"},on:{click:function(n){return e.handleUpdate(t.row)}}},[e._v("修改")]),n("el-button",{directives:[{name:"hasPermi",rawName:"v-hasPermi",value:["system:notice:remove"],expression:"['system:notice:remove']"}],attrs:{size:"mini",type:"text",icon:"el-icon-delete"},on:{click:function(n){return e.handleDelete(t.row)}}},[e._v("删除")]),n("el-button",{directives:[{name:"hasPermi",rawName:"v-hasPermi",value:["system:notice:query"],expression:"['system:notice:query']"}],attrs:{size:"mini",type:"text",icon:"el-icon-folder-opened"},on:{click:function(n){return e.fileList(t.row)}}},[e._v("资源列表")])]}}])})],1),n("pagination",{directives:[{name:"show",rawName:"v-show",value:e.total>0,expression:"total>0"}],attrs:{total:e.total,page:e.queryParams.pageNum,limit:e.queryParams.pageSize},on:{"update:page":function(t){return e.$set(e.queryParams,"pageNum",t)},"update:limit":function(t){return e.$set(e.queryParams,"pageSize",t)},pagination:e.getList}}),n("el-dialog",{attrs:{title:e.title,visible:e.open,"before-close":e.cancel,width:"780px","append-to-body":""},on:{"update:visible":function(t){e.open=t}}},[n("el-form",{ref:"form",attrs:{model:e.form,rules:e.rules,"label-width":"80px"}},[n("el-row",[n("el-col",{attrs:{span:12}},[n("el-form-item",{attrs:{label:"公告标题",prop:"noticeTitle"}},[n("el-input",{attrs:{placeholder:"请输入公告标题"},model:{value:e.form.noticeTitle,callback:function(t){e.$set(e.form,"noticeTitle",t)},expression:"form.noticeTitle"}})],1)],1),n("el-col",{attrs:{span:12}},[n("el-form-item",{attrs:{label:"公告类型",prop:"noticeType"}},[n("el-select",{attrs:{placeholder:"请选择"},model:{value:e.form.noticeType,callback:function(t){e.$set(e.form,"noticeType",t)},expression:"form.noticeType"}},e._l(e.dict.type.sys_notice_type,(function(e){return n("el-option",{key:e.value,attrs:{label:e.label,value:e.value}})})),1)],1)],1),n("el-col",{attrs:{span:24}},[n("el-form-item",{attrs:{label:"状态"}},[n("el-radio-group",{model:{value:e.form.status,callback:function(t){e.$set(e.form,"status",t)},expression:"form.status"}},e._l(e.dict.type.sys_notice_status,(function(t){return n("el-radio",{key:t.value,attrs:{label:t.value}},[e._v(" "+e._s(t.label)+" ")])})),1)],1)],1),n("el-col",{attrs:{span:24}},[n("el-form-item",{attrs:{label:"内容"}},[n("el-input",{style:{width:"100%"},attrs:{type:"textarea",autosize:{minRows:4,maxRows:15}},on:{getFileId:e.getFileId},model:{value:e.form.noticeContent,callback:function(t){e.$set(e.form,"noticeContent",t)},expression:"form.noticeContent"}})],1)],1)],1)],1),n("div",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[n("el-button",{attrs:{type:"primary"},on:{click:e.submitForm}},[e._v("确 定")]),n("el-button",{on:{click:e.cancel}},[e._v("取 消")])],1)],1),n("el-dialog",{attrs:{title:e.title,visible:e.fileListOpen,width:"1000px","append-to-body":""},on:{"update:visible":function(t){e.fileListOpen=t}}},[n("el-table",{directives:[{name:"loading",rawName:"v-loading",value:e.loading,expression:"loading"}],attrs:{data:e.fileInfoList}},[n("el-table-column",{attrs:{type:"selection",width:"55",align:"center"}}),n("el-table-column",{attrs:{label:"图片预览",align:"center",prop:"pic"},scopedSlots:e._u([{key:"default",fn:function(e){return[n("el-image",{staticStyle:{width:"120px",height:"60px"},attrs:{src:e.row.pic,lazy:"","preview-src-list":[e.row.pic]}})]}}])}),n("el-table-column",{attrs:{label:"文件名称",align:"center",prop:"fileOriginName"}}),n("el-table-column",{attrs:{label:"文件类型",align:"center",prop:"fileSuffix"}}),n("el-table-column",{attrs:{label:"文件大小",align:"center",prop:"fileSizeInfo"}}),n("el-table-column",{attrs:{label:"操作",align:"center","class-name":"small-padding fixed-width"},scopedSlots:e._u([{key:"default",fn:function(t){return[n("el-button",{attrs:{size:"mini",type:"text",icon:"el-icon-download"},on:{click:function(n){return e.handleDownload(t.row)}}},[e._v("下载")])]}}])})],1)],1)],1)},a=[],o=(n("d81d"),n("a573"),n("8b29")),l=n("3be1"),r=n("b775");function s(e){return Object(r["a"])({url:"/fileNoticeInfo",method:"post",data:e})}function c(e){return Object(r["a"])({url:"/fileNoticeInfo/"+e,method:"delete"})}function u(e){return Object(r["a"])({url:"/fileNoticeInfo/"+e,method:"get"})}var d={name:"Notice",dicts:["sys_notice_status","sys_notice_type"],data:function(){return{loading:!0,ids:[],single:!0,multiple:!0,showSearch:!0,total:0,noticeList:[],fileInfoList:[],title:"",open:!1,fileListOpen:!1,queryParams:{pageNum:1,pageSize:10,noticeTitle:void 0,createBy:void 0,status:void 0},form:{},rules:{noticeTitle:[{required:!0,message:"公告标题不能为空",trigger:"blur"}],noticeType:[{required:!0,message:"公告类型不能为空",trigger:"change"}]},fileIds:[]}},created:function(){this.getList()},methods:{getList:function(){var e=this;this.loading=!0,Object(o["d"])(this.queryParams).then((function(t){e.noticeList=t.rows,e.total=t.total,e.loading=!1}))},cancel:function(){var e=this;this.$confirm("是否放弃此次编辑？","系统提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then((function(){var t=e.fileIds;t.length>0&&Object(l["b"])(t),e.fileIds.length=0,e.open=!1,e.reset()})).catch((function(){}))},reset:function(){this.form={noticeId:void 0,noticeTitle:void 0,noticeType:void 0,noticeContent:void 0,status:"0"},this.resetForm("form")},handleQuery:function(){this.queryParams.pageNum=1,this.getList()},resetQuery:function(){this.resetForm("queryForm"),this.handleQuery()},handleSelectionChange:function(e){this.ids=e.map((function(e){return e.noticeId})),this.single=1!=e.length,this.multiple=!e.length},handleAdd:function(){this.reset(),this.open=!0,this.title="添加公告"},handleUpdate:function(e){var t=this;this.reset();var n=e.noticeId||this.ids;Object(o["c"])(n).then((function(e){t.form=e.data,t.open=!0,t.title="修改公告"}))},submitForm:function(){var e=this;this.$refs["form"].validate((function(t){t&&(void 0!=e.form.noticeId?Object(o["e"])(e.form).then((function(t){if(e.fileIds.length>0){var n={noticeId:e.form.noticeId,fileIds:e.fileIds};s(n).then((function(e){}))}e.$modal.msgSuccess("修改成功"),e.fileIds.length=0,e.open=!1,e.getList()})):Object(o["a"])(e.form).then((function(t){if(e.fileIds.length>0){var n={noticeId:t.data,fileIds:e.fileIds};s(n).then((function(e){}))}e.$modal.msgSuccess("新增成功"),e.fileIds.length=0,e.open=!1,e.getList()})))}))},handleDelete:function(e){var t=this,n=e.noticeId||this.ids;this.$modal.confirm('是否确认删除公告编号为"'+n+'"的数据项？').then((function(){return c(n).then().then((function(e){})),Object(o["b"])(n)})).then((function(){t.getList(),t.$modal.msgSuccess("删除成功")})).catch((function(){}))},getFileId:function(e){this.fileIds.push(e)},fileList:function(e){var t=this;this.loading=!0,this.reset();var n=e.noticeId||this.ids;u(n).then((function(e){for(var n=0;n<e.data.length;n++){var i=e.data[n];switch(i.fileSuffix){case"png":case"jpg":case"jpeg":case"bmp":case"gif":e.data[n].pic="/prod-api"+i.filePath;break;default:e.data[n].pic=image.bg1;break}}t.fileInfoList=e.data,t.fileListOpen=!0,t.title="资源列表",t.loading=!1}))},handleDownload:function(e){var t=e.fileOriginName,n=e.filePath,i=(n.substring(n.lastIndexOf("."),n.length),document.createElement("a"));i.setAttribute("download",t),i.setAttribute("target","_blank"),i.setAttribute("href","/prod-api"+n),i.click()}}},m=d,f=n("2877"),p=Object(f["a"])(m,i,a,!1,null,null,null);t["default"]=p.exports},"3be1":function(e,t,n){"use strict";n.d(t,"c",(function(){return a})),n.d(t,"d",(function(){return o})),n.d(t,"a",(function(){return l})),n.d(t,"e",(function(){return r})),n.d(t,"b",(function(){return s}));var i=n("b775");function a(e){return Object(i["a"])({url:"/fileInfo/list",method:"get",params:e})}function o(e){return Object(i["a"])({url:"/cms/blog/cms/fileInfo/list",method:"get",params:e})}function l(e){return Object(i["a"])({url:"/fileInfo",method:"post",data:e})}function r(e){return Object(i["a"])({url:"/fileInfo",method:"put",data:e})}function s(e){return Object(i["a"])({url:"/fileInfo/"+e,method:"delete"})}},"8b29":function(e,t,n){"use strict";n.d(t,"d",(function(){return a})),n.d(t,"c",(function(){return o})),n.d(t,"a",(function(){return l})),n.d(t,"e",(function(){return r})),n.d(t,"b",(function(){return s}));var i=n("b775");function a(e){return Object(i["a"])({url:"/system/notice/list",method:"get",params:e})}function o(e){return Object(i["a"])({url:"/system/notice/"+e,method:"get"})}function l(e){return Object(i["a"])({url:"/system/notice",method:"post",data:e})}function r(e){return Object(i["a"])({url:"/system/notice",method:"put",data:e})}function s(e){return Object(i["a"])({url:"/system/notice/"+e,method:"delete"})}}}]);