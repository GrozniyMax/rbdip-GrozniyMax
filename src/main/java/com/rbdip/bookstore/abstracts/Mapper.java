package com.rbdip.bookstore.abstracts;

public interface Mapper<FROM, TO> {

    public TO map(FROM from);

    public FROM unmap(TO to);
}
