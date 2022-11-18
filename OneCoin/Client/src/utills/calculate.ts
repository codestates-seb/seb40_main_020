/**
 * 현재 가격, 계산할 가격, 수량을 입력하면 실시간 수익금을 리턴합니다.
 * @param {number} currentPrice 현재 시장의 실시간 가격
 * @param {number} buyPrice 계산할 가격 ex) 평균 매수가 or 시가
 * @param {number} quantity 수량
 * @returns 실시간 수익금을 리턴합니다.
 */
function proceedsCalculator(
	currentPrice: number,
	buyPrice: number,
	quantity = 1
): number {
	return (currentPrice - buyPrice) * quantity;
}

/**
 * 현재 가격, 계산할 가격을 입력하면 실시간 수익률을 리턴합니다.
 * @param {number} currentPrice 현재 시장의 실시간 가격
 * @param {number} buyPrice 계산할 가격 ex) 평균 매수가 or 시가
 * @returns 실시간 수익률을 리턴합니다.
 */
function yieldCalculator(currentPrice: number, buyPrice: number): number {
	return ((currentPrice - buyPrice) / buyPrice) * 100;
}

export { proceedsCalculator, yieldCalculator };
