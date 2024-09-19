//package com.techlabs.app.service;
//
//import java.time.LocalDateTime;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Objects;
//import java.util.Set;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//import com.stripe.Stripe;
//import com.stripe.exception.StripeException;
//import com.stripe.model.PaymentIntent;
//import com.techlabs.app.dto.PaymentDto;
//import com.techlabs.app.entity.Agent;
//import com.techlabs.app.entity.Commission;
//import com.techlabs.app.entity.CommissionType;
//import com.techlabs.app.entity.DocumentStatus;
//import com.techlabs.app.entity.InsurancePolicy;
//import com.techlabs.app.entity.InsuranceScheme;
//import com.techlabs.app.entity.Payment;
//import com.techlabs.app.entity.PaymentStatus;
//import com.techlabs.app.entity.PaymentType;
//import com.techlabs.app.entity.PolicyStatus;
//import com.techlabs.app.entity.SubmittedDocument;
//import com.techlabs.app.exception.APIException;
//import com.techlabs.app.exception.LifeGuardException;
//import com.techlabs.app.repository.AgentRepository;
//import com.techlabs.app.repository.CommissionRepository;
//import com.techlabs.app.repository.InsurancePolicyRepository;
//import com.techlabs.app.repository.InsuranceSchemeRepository;
//import com.techlabs.app.repository.PaymentRepository;
//import com.techlabs.app.repository.SubmittedDocumentRepository;
//
//@Service
//public class StripePaymentServiceImpl implements StripePaymentService {
//
//	@Value("${stripe.secret.key}")
//	private String stripeSecretKey;
//
//	@Autowired
//	private InsurancePolicyRepository policyRepository;
//	@Autowired
//	private PaymentRepository paymentRepository;
//	@Autowired
//	private CommissionRepository commissionRepository;
//	@Autowired
//	private AgentRepository agentRepository;
//
//	@Autowired
//	private InsurancePolicyRepository insurancePolicyRepository;
//
//	@Autowired
//	private SubmittedDocumentRepository submittedDocumentRepository;
//
//	@Override
//	public PaymentIntent createPaymentIntent(PaymentDto paymentDto, Payment payment) throws StripeException {
//		Stripe.apiKey = stripeSecretKey;
//
//		Map<String, Object> params = new HashMap<>();
//		params.put("amount", (int) (paymentDto.getTotalPayment() * 100));
//		params.put("currency", "inr");
//		params.put("payment_method", paymentDto.getPaymentMethodId());
//
//		InsurancePolicy policy = policyRepository.findById(paymentDto.getPolicyId()).orElseThrow(
//				() -> new LifeGuardException("Policy with ID : " + paymentDto.getPolicyId() + " cannot be found"));
//
//		payment.setPolicy(policy);
//
//		double totalPaidAmount = paymentDto.getAmount() + policy.getTotalAmountPaid();
//
//		if (totalPaidAmount > policy.getPremiumAmount()) {
//			throw new LifeGuardException("Your payment is complete. You can now claim your policy.");
//		}
//
//		if (paymentDto.getPaymentType().equalsIgnoreCase("debit")) {
//			payment.setPaymentType(PaymentType.DEBIT_CARD.name());
//		} else {
//			payment.setPaymentType(PaymentType.CREDIT_CARD.name());
//		}
//
//		payment.setAmount(paymentDto.getAmount());
//		payment.setTax(paymentDto.getTax());
//		payment.setTotalPayment(paymentDto.getTotalPayment());
//		payment.setPaymentDate(LocalDateTime.now());
//
//		if (totalPaidAmount >= policy.getPremiumAmount()) {
//			policy.setPolicyStatus(PolicyStatus.COMPLETE.name());
//		} else if (PolicyStatus.PENDING.name().equals(policy.getPolicyStatus())) {
//			policy.setPolicyStatus(PolicyStatus.ACTIVE.name());
//
//			updateSubmittedDocumentsStatus(policy);
//		}
//
//		if (!policy.isVerified()) {
//			policy.setVerified(true);
//		}
//
//		payment.setPaymentStatus(PaymentStatus.PAID.name());
//
//		policy.getPayments().add(payment);
//		policy.setTotalAmountPaid(totalPaidAmount);
//
//		Agent agent = policy.getAgent();
//		if (agent != null && agent.isActive() && agent.isVerified()) {
//			handleAgentCommission(agent, policy, paymentDto.getAmount());
//		}
//
//		policyRepository.save(policy);
//		paymentRepository.save(payment);
//
//		return PaymentIntent.create(params);
//	}
//
//	private void updateSubmittedDocumentsStatus(InsurancePolicy policy) {
//		Set<SubmittedDocument> documents = policy.getDocuments();
//
//		for (SubmittedDocument document : documents) {
//			if (DocumentStatus.PENDING.name().equals(document.getDocumentStatus())) {
//				document.setDocumentStatus(DocumentStatus.APPROVED.name());
//				submittedDocumentRepository.save(document);
//			}
//		}
//	}
//
//	private void handleAgentCommission(Agent agent, InsurancePolicy policy, double paymentAmount) {
//
//		InsuranceScheme insuranceScheme = insurancePolicyRepository
//				.findInsuranceSchemeByPolicyId(policy.getInsuranceId());
//		double installmentRatio = insuranceScheme.getInstallmentPaymentCommission();
//		double commissionAmount = (paymentAmount * installmentRatio) / 100;
//
//		Commission commission = new Commission();
//		commission.setCommissionType(CommissionType.INSTALLMENT.name());
//		commission.setAmount(commissionAmount);
//		commission.setAgent(agent);
//		commission.setInsurancePolicy(policy);
//		Commission savedCommission = commissionRepository.save(commission);
//
//		Set<Commission> commissions = agent.getCommissions();
//		commissions.add(savedCommission);
//
//		double totalCommission = commissions.stream().mapToDouble(Commission::getAmount).sum();
//		agent.setTotalCommission(totalCommission);
//
//		agentRepository.save(agent);
//	}
//
//}
