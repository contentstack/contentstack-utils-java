package com.contentstack.utils.embedded;

/**
 * There are 3 ways (BLOCK, INLINE, LINK) of attaching entries inside the RTE
 * field as mentioned in requirements.
 * And Two Ways ( DISPLAY) for assets
 *
 * [`Example`]:
 *
 * For `Entry`: StyleType.BLOCK, StyleType.INLINE, StyleType.LINK,
 * For `Asset`: StyleType.DISPLAY
 */
public enum StyleType {
    BLOCK, INLINE, LINK, DISPLAY
}
