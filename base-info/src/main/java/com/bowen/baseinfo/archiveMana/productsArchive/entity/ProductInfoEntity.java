package com.bowen.baseinfo.archiveMana.productsArchive.entity;

import java.io.Serializable;
import lombok.Data;
import java.time.LocalDateTime;


/**
 * @Description  
 * @Author  dw
 * @Date 2024-04-08 
 */
@Data
public class ProductInfoEntity implements Serializable {

	private static final long serialVersionUID =  3313259404023204234L;

	/**
	 * 商品ID
	 */
	private String productId;

	/**
	 * 商品名称
	 */
	private String productName;

	/**
	 * 商品简名
	 */
	private String productAbbr;

	/**
	 * 助记码
	 */
	private String mnemonicCode;

	/**
	 * 包装规格
	 */
	private String productPkgSpec;

	/**
	 * 销售规格
	 */
	private String salesSpec;

	/**
	 * 产地
	 */
	private String placeOfOrigin;

	/**
	 * 条形码
	 */
	private String barCode;

	/**
	 * 份额
	 */
	private String share;

	/**
	 * 主计量单位
	 */
	private String mainMeasUnit;

	/**
	 * 辅计量单位
	 */
	private String secMeasUnit;

	/**
	 * 辅计量毛重
	 */
	private String secMeasGrossWeight;

	/**
	 * 计量单位换算
	 */
	private String measUnitConvert;

	/**
	 * 商品分类
	 */
	private String productType;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 销售,采购,附件,影响库存<当销售或采购的属性没有勾选时,此商品在销售或采购查询商品的时候无法查询出来>
	 */
	private String productAttr;

	/**
	 * 默认仓库
	 */
	private String defaultRepo;

	/**
	 * 创建人
	 */
	private String createdBy;

	/**
	 * 创建时间
	 */
	private LocalDateTime createdTime;

	/**
	 * 更新人
	 */
	private String updatedBy;

	/**
	 * 更新时间
	 */
	private LocalDateTime updatedTime;

}
