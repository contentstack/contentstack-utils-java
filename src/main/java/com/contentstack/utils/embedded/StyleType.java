package com.contentstack.utils.embedded;

/**
 * There are 3 ways (BLOCK, INLINE, LINKED) of attaching entries inside the RTE field as mentioned in requirements.
 * And Two Ways (DOWNLOADABLE, DISPLAYABLE) for assets
 *
 * [`Example`]:
 *
 * For `Entry`: StyleType.BLOCK, StyleType.INLINE, StyleType.LINKED,
 * For `Asset`: StyleType.DOWNLOADABLE, StyleType.DISPLAYABLE
 */
public enum StyleType {
    BLOCK, INLINE, LINKED, DISPLAY
}
