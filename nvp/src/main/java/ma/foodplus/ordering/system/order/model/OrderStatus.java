package ma.foodplus.ordering.system.order.model;

public enum OrderStatus {
    DRAFT,          // مسودة الطلب
    PENDING,        // في انتظار التأكيد
    CONFIRMED,      // تم تأكيد الطلب
    PREPARING,      // قيد التحضير
    READY,          // جاهز للتوصيل
    DELIVERING,     // قيد التوصيل
    DELIVERED,      // تم التوصيل
    CANCELLED,      // تم الإلغاء
    REFUNDED;       // تم استرداد المبلغ

    public OrderStatusTransition getTransition() {
        return OrderStatusTransition.valueOf(this.name());
    }
} 