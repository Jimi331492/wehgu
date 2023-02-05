package com.wehgu.admin.common;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p> 基类 </p>
 *
 */
@RestController
@Slf4j
public class BaseController {

    public Logger LOG = LoggerFactory.getLogger(getClass());

}
