package model.order_invoice;

import exception.DataAccessException;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface OrderInvoiceDao {
    OrderInvoice findById(int id) throws DataAccessException;
    OrderInvoice create(int orderId, LocalDateTime createdAt, LocalDate dueDate, int toPay, int hasPaid);
}
