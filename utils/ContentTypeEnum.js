const ContentTypeEnum = Object.freeze({
  'Form_Sub':Symbol("表单提交"),
  'Json_Sub': Symbol('Json提交'),
  'Upload_Sub': Symbol('文件上传提交'),
  'Default_Sub': Symbol('默认提交')
});

module.exports=ContentTypeEnum;