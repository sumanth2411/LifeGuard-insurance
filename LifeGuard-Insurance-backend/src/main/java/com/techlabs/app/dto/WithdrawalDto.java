package com.techlabs.app.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class WithdrawalDto {

  private Long withdrawalId;
  
  private LocalDateTime withdrawalDate;
  
  @NotNull
  @PositiveOrZero(message = "Amount Should be Greater than Zero")
  private Double amount;

  private String status;
  private Double leftCommission;
  private AgentRequestDto agentDto;
public Long getWithdrawalId() {
	return withdrawalId;
}
public void setWithdrawalId(Long withdrawalId) {
	this.withdrawalId = withdrawalId;
}
public LocalDateTime getWithdrawalDate() {
	return withdrawalDate;
}
public void setWithdrawalDate(LocalDateTime withdrawalDate) {
	this.withdrawalDate = withdrawalDate;
}
public Double getAmount() {
	return amount;
}
public void setAmount(Double amount) {
	this.amount = amount;
}
public String getStatus() {
	return status;
}
public void setStatus(String status) {
	this.status = status;
}
public Double getLeftCommission() {
	return leftCommission;
}
public void setLeftCommission(Double leftCommission) {
	this.leftCommission = leftCommission;
}
public AgentRequestDto getAgentDto() {
	return agentDto;
}
public void setAgentDto(AgentRequestDto agentDto) {
	this.agentDto = agentDto;
}
  
  
}