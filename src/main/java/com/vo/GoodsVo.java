package com.vo;

import com.domain.Goods;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 温和的洛瑞
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class GoodsVo extends Goods {
    private static final long serialVersionUID = 1L;

    private Integer page=1;
    private Integer limit=10;
}
