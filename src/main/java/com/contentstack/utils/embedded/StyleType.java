package com.contentstack.utils.embedded;

/**
 * -There are 5 ways of attaching entries inside the RTE field.
 * `Block`:  Useful for things like image carousels, embedded media, forms, product listings
 * basically anything that does not make sense inline
 * `Inline`:  Useful for passing in some value from a linked entry that might be dynamic,
 * such as a working title, a link that opens a modal popup, or any other use case
 * where a developer might want an entire entry"\u00a9s" content to be embedded inline.
 * `Linked`: Relative link to an entry object
 * <p>
 * -There are Two way of attaching assets inside the RTE field.
 * <p>
 * `DISPLAY`:
 * <p>
 * `DOWNLOAD`:
 * <p>
 * [`Example`]:
 * <p>
 * For `Entry`: StyleType.BLOCK, StyleType.INLINE, StyleType.LINKED,
 * For `Assets`: StyleType.DISPLAY and StyleType.DOWNLOAD
 */
public enum StyleType {
    BLOCK, INLINE, LINK, DISPLAY, DOWNLOAD,
}