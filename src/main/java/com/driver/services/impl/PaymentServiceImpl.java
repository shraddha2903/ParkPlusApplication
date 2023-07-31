package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.PaymentRepository;
import com.driver.repository.ReservationRepository;
import com.driver.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    ReservationRepository reservationRepository2;
    @Autowired
    PaymentRepository paymentRepository2;

    @Override
    public Payment pay(Integer reservationId, int amountSent, String mode) throws Exception {

        Reservation reservation =reservationRepository2.findById(reservationId).get();
        Spot spot = reservation.getSpot();
        int bill =reservation.getNumberOfHours()* spot.getPricePerHour();

        Payment payment = new Payment();
        payment.setReservation(reservation);

        String updatedMode = mode.toUpperCase();
        payment.setIsPaymentCompleted(false);

        if (updatedMode.equals("CASH")){
            payment.setPaymentMode(PaymentMode.CASH);
        } else if (updatedMode.equals("CARD")) {
            payment.setPaymentMode(PaymentMode.CARD);

        } else if (updatedMode.equals("UPI")) {
            payment.setPaymentMode(PaymentMode.UPI);
        }
        else {
            throw new Exception("Payment mode not detected");
        }

        spot.setOccupied(false);
        payment.setIsPaymentCompleted(true);
        payment.setReservation(reservation);

        reservation.setPayment(payment);

        reservationRepository2.save(reservation);
        return payment;
    }
}
