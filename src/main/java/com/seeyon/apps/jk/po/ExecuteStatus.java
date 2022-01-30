package com.seeyon.apps.jk.po;

/**
 * <p>TODO</p>
 *
 * @author DELk
 * @@version 1.0.0
 * @since 2022/1/30
 */
public enum ExecuteStatus {
    EXECUTING,  /** 执行中 */
    STOP,       /** 终止 */
    END,        /** 正常结束 */
    ENDWITHEX,  /** 异常结束 */
}
