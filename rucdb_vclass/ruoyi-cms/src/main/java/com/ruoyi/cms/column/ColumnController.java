package com.ruoyi.cms.column;

import com.ruoyi.cms.column.service.IColumnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cms/column")
public class ColumnController {
    @Autowired
    IColumnService columnService;
}
