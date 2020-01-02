
CREATE TABLE `merchant` (
  `id` varchar(16) NOT NULL COMMENT '商户号。前4位地区码 + 12位序号',
  `name` varchar(64) NOT NULL DEFAULT '' COMMENT '商户名称',
  `abbr` varchar(32) NOT NULL DEFAULT '' COMMENT '商户简称',
  `status` tinyint(4) NOT NULL COMMENT '状态。1-可用；0-初始；2-暂停；3-新增待审核；4-变更待审核；5-注销待审核；9-销户',
  `province` varchar(32) NOT NULL DEFAULT '' COMMENT '省份。地址表主键',
  `city` varchar(32) NOT NULL DEFAULT '' COMMENT '城市。地址表主键',
  `business_type` tinyint(4) NOT NULL COMMENT '业务类型。1-扫码支付；2-被扫支付；3-扫码与被扫支付',
  `order_valid_time` int(11) NOT NULL COMMENT '订单有效时间。单位：分钟。主扫默认5分钟。',
  `single_limit` bigint(20) unsigned NOT NULL COMMENT '单笔交易限额。单位：分',
  `day_limit` bigint(20) unsigned NOT NULL COMMENT '日累计限额。单位：分',
  `has_device` tinyint(4) NOT NULL COMMENT '是否维护设备信息。1-维护；0-不维护',
  `credit_card_limit` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否限制信用卡。1-限制；0-不限制',
  `secure_key` varchar(256) NOT NULL COMMENT '密钥',
  `sign_type` varchar(8) NOT NULL COMMENT '签名方式。MD5',
  `reconciliation_type` tinyint(4) NOT NULL COMMENT '对账模式。1-TVM对账；0-非TVM对账',
  `upload_recon_file` tinyint(4) NOT NULL COMMENT '是否上送对账文件。1-上送；0-不上送',
  `upload_recon_url` varchar(256) NOT NULL DEFAULT '' COMMENT '对账文件上传路径',
  `create_user` int(11) NOT NULL COMMENT '创建人',
  `modified_user` int(11) NOT NULL COMMENT '更新人',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商户信息表';

CREATE TABLE `merchant_route` (
  `id` int(11) NOT NULL COMMENT '主键',
  `merchant_id` varchar(16) NOT NULL COMMENT '商户ID',
  `channel_merchant_id` int(11) DEFAULT NULL COMMENT 'channel_merchant表主键',
  `status` tinyint(4) DEFAULT NULL COMMENT '状态。1-生效；0-失效',
  `create_user` int(11) DEFAULT NULL COMMENT '创建人',
  `modified_user` int(11) DEFAULT NULL COMMENT '更新人',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_mer_route` (`merchant_id`,`channel_merchant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商户渠道路由表';

CREATE TABLE `channel_merchant` (
  `id` int(11) NOT NULL COMMENT '主键',
  `channel_id` tinyint(4) NOT NULL COMMENT '渠道ID。1-银联；',
  `channel_mer_id` varchar(32) NOT NULL COMMENT '渠道商户号',
  `fee_rate` decimal(5,2) NOT NULL DEFAULT '1.00' COMMENT '费率',
  `secure_key` varchar(256) NOT NULL COMMENT '安全密钥',
  `secure_path` varchar(32) NOT NULL DEFAULT '' COMMENT '安全证书路径',
  `region` varchar(32) NOT NULL DEFAULT '' COMMENT '区域代码',
  `status` tinyint(4) NOT NULL COMMENT '状态。1-生效；0-失效；',
  `base_url` varchar(256) NOT NULL DEFAULT '' COMMENT '渠道请求地址',
  `sign_type` varchar(8) NOT NULL COMMENT '签名类型。MD5、RSA、RSA2等。',
  `app_id` varchar(64) NOT NULL DEFAULT '' COMMENT '支付宝开放平台应用ID；微信公众号APPID',
  `plat_pri_key` varchar(2048) NOT NULL DEFAULT '' COMMENT '聚合支付平台私钥',
  `plat_pub_key` varchar(1024) NOT NULL DEFAULT '' COMMENT '聚合支付平台公钥',
  `channel_pub_key` varchar(1024) NOT NULL DEFAULT '' COMMENT '渠道公钥',
  `channel_mer_name` varchar(64) NOT NULL DEFAULT '' COMMENT '渠道商户名称',
  `channel_mer_abbr` varchar(32) NOT NULL DEFAULT '' COMMENT '渠道商户简称',
  `remark` varchar(128) NOT NULL DEFAULT '' COMMENT '备注',
  `create_user` int(11) NOT NULL COMMENT '创建人',
  `modified_user` int(11) NOT NULL COMMENT '更新人',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='渠道商户信息表';

CREATE TABLE `device_info` (
  `id` int(11) NOT NULL COMMENT '主键',
  `no` varchar(16) NOT NULL COMMENT '设备编号',
  `merchant_id` varchar(32) NOT NULL COMMENT '商户号',
  `type` tinyint(4) NOT NULL DEFAULT '1' COMMENT '设备类型',
  `line_no` varchar(32) NOT NULL DEFAULT '' COMMENT '线路。地铁线路',
  `site_no` varchar(32) NOT NULL DEFAULT '' COMMENT '站点编号',
  `site_name` varchar(32) NOT NULL DEFAULT '' COMMENT '站点名称',
  `status` tinyint(4) NOT NULL COMMENT '状态。1-生效；0-失效；',
  `create_user` int(11) NOT NULL COMMENT '创建人',
  `modified_user` int(11) NOT NULL COMMENT '更新人',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='设备信息表';



