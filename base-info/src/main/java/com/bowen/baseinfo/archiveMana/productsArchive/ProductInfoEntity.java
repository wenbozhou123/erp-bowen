package com.bowen.baseinfo.archiveMana.productsArchive;

import java.time.LocalDateTime;
import java.time.LocalDateTime;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import java.io.Serializable;

/**
 * @Description  
 * @Author  ZhouBowen
 * @Date 2024-04-10 19:08:19 
 */
@Entity
@Table(name="product_info")
public class ProductInfoEntity implements Serializable  {
	private static final long serialVersionUID =  4583296144679541884L;

	/**
	 * 商品ID
	 */
	@Id
	@Column(name="product_id")
	private String productId;

	/**
	 * 商品名称
	 */
	@Column(name="product_name")
	private String productName;

	/**
	 * 商品简名
	 */
	@Column(name="product_abbr")
	private String productAbbr;

	/**
	 * 助记码
	 */
	@Column(name="mnemonic_code")
	private String mnemonicCode;

	/**
	 * 包装规格
	 */
	@Column(name="product_pkg_spec")
	private String productPkgSpec;

	/**
	 * 销售规格
	 */
	@Column(name="sales_spec")
	private String salesSpec;

	/**
	 * 产地
	 */
	@Column(name="place_of_origin")
	private String placeOfOrigin;

	/**
	 * 条形码
	 */
	@Column(name="bar_code")
	private String barCode;

	/**
	 * 份额
	 */
	@Column(name="share")
	private Object share;

	/**
	 * 主计量单位
	 */
	@Column(name="main_meas_unit")
	private String mainMeasUnit;

	/**
	 * 辅计量单位
	 */
	@Column(name="sec_meas_unit")
	private String secMeasUnit;

	/**
	 * 辅计量毛重
	 */
	@Column(name="sec_meas_gross_weight")
	private String secMeasGrossWeight;

	/**
	 * 计量单位换算
	 */
	@Column(name="meas_unit_convert")
	private Object measUnitConvert;

	/**
	 * 商品分类
	 */
	@Column(name="product_type")
	private String productType;

	/**
	 * 备注
	 */
	@Column(name="remark")
	private String remark;

	/**
	 * 销售,采购,附件,影响库存<当销售或采购的属性没有勾选时,此商品在销售或采购查询商品的时候无法查询出来>
	 */
	@Column(name="product_attr")
	private String productAttr;

	/**
	 * 默认仓库
	 */
	@Column(name="default_repo")
	private String defaultRepo;

	/**
	 * 创建人
	 */
	@Column(name="created_by")
	private String createdBy;

	/**
	 * 创建时间
	 */
	@Column(name="created_time")
	private LocalDateTime createdTime;

	/**
	 * 更新人
	 */
	@Column(name="updated_by")
	private String updatedBy;

	/**
	 * 更新时间
	 */
	@Column(name="updated_time")
	private LocalDateTime updatedTime;

	public String getProductId() {
		return this.productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return this.productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductAbbr() {
		return this.productAbbr;
	}

	public void setProductAbbr(String productAbbr) {
		this.productAbbr = productAbbr;
	}

	public String getMnemonicCode() {
		return this.mnemonicCode;
	}

	public void setMnemonicCode(String mnemonicCode) {
		this.mnemonicCode = mnemonicCode;
	}

	public String getProductPkgSpec() {
		return this.productPkgSpec;
	}

	public void setProductPkgSpec(String productPkgSpec) {
		this.productPkgSpec = productPkgSpec;
	}

	public String getSalesSpec() {
		return this.salesSpec;
	}

	public void setSalesSpec(String salesSpec) {
		this.salesSpec = salesSpec;
	}

	public String getPlaceOfOrigin() {
		return this.placeOfOrigin;
	}

	public void setPlaceOfOrigin(String placeOfOrigin) {
		this.placeOfOrigin = placeOfOrigin;
	}

	public String getBarCode() {
		return this.barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public Object getShare() {
		return this.share;
	}

	public void setShare(Object share) {
		this.share = share;
	}

	public String getMainMeasUnit() {
		return this.mainMeasUnit;
	}

	public void setMainMeasUnit(String mainMeasUnit) {
		this.mainMeasUnit = mainMeasUnit;
	}

	public String getSecMeasUnit() {
		return this.secMeasUnit;
	}

	public void setSecMeasUnit(String secMeasUnit) {
		this.secMeasUnit = secMeasUnit;
	}

	public String getSecMeasGrossWeight() {
		return this.secMeasGrossWeight;
	}

	public void setSecMeasGrossWeight(String secMeasGrossWeight) {
		this.secMeasGrossWeight = secMeasGrossWeight;
	}

	public Object getMeasUnitConvert() {
		return this.measUnitConvert;
	}

	public void setMeasUnitConvert(Object measUnitConvert) {
		this.measUnitConvert = measUnitConvert;
	}

	public String getProductType() {
		return this.productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getProductAttr() {
		return this.productAttr;
	}

	public void setProductAttr(String productAttr) {
		this.productAttr = productAttr;
	}

	public String getDefaultRepo() {
		return this.defaultRepo;
	}

	public void setDefaultRepo(String defaultRepo) {
		this.defaultRepo = defaultRepo;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public LocalDateTime getCreatedTime() {
		return this.createdTime;
	}

	public void setCreatedTime(LocalDateTime createdTime) {
		this.createdTime = createdTime;
	}

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public LocalDateTime getUpdatedTime() {
		return this.updatedTime;
	}

	public void setUpdatedTime(LocalDateTime updatedTime) {
		this.updatedTime = updatedTime;
	}

}

