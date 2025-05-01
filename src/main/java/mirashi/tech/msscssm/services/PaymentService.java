package mirashi.tech.msscssm.services;

import mirashi.tech.msscssm.domain.Payment;
import mirashi.tech.msscssm.domain.PaymentEvent;
import mirashi.tech.msscssm.domain.PaymentState;
import org.springframework.statemachine.StateMachine;

public interface PaymentService {
    Payment newPayment(Payment payment);
    StateMachine<PaymentState, PaymentEvent> preAuth(Long paymentId);
    StateMachine<PaymentState, PaymentEvent> authorizePayment(Long paymentId);
    StateMachine<PaymentState, PaymentEvent> declineAuth(Long paymentId);

}
