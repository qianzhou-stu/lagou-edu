package com.lagou.common.page;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("分页数据结果集 VO")
public class DataGrid<T> {
    @ApiModelProperty("总记录数")
    private Long total = 0L;

    @ApiModelProperty("总页数数")
    private Long totalPages = 0L;

    @ApiModelProperty("总页数数")
    private Long currentPage = 0L;

    @ApiModelProperty("本页数据记录")
    private List<T> rows = new ArrayList<>();
}
