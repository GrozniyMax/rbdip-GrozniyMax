package com.rbdip.bookstore.review.service;

/**
 * Порт модуля review (ЛР4, Strangler Fig): проверка того, что клиент реально
 * покупал товар, выполняется не напрямую во внутренностях пакета order,
 * а через этот контракт. Реализацию предоставляет модуль order
 * (OrderPurchaseVerifier), что убирает зависимость review -> order.
 */
public interface PurchaseVerifier {

    boolean isVerifiedPurchase(Long productId);
}