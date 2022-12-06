/**
 * 현재 가격, 계산할 가격, 수량을 입력하면 실시간 수익금을 리턴합니다.
 * @param {number} currentPrice 현재 시장의 실시간 가격
 * @param {number} buyPrice 계산할 가격 ex) 평균 매수가 or 시가
 * @param {number} quantity 수량
 * @returns 실시간 수익금을 리턴합니다.
 */
export function proceedsCalculator(
	currentPrice: number,
	buyPrice: number,
	quantity = 1
): number {
	return Math.round(+((currentPrice - buyPrice) * quantity));
}

/**
 * 현재 가격, 계산할 가격을 입력하면 실시간 수익률을 리턴합니다.
 * @param {number} currentPrice 현재 시장의 실시간 가격
 * @param {number} buyPrice 계산할 가격 ex) 평균 매수가 or 시가
 * @returns 실시간 수익률을 리턴합니다.
 */
export function rateCalculator(currentPrice: number, buyPrice: number): number {
	return +(((currentPrice - buyPrice) / buyPrice) * 100).toFixed(2);
}

export const dateCalc = (t: string) => {
	const date = new Date(t);
	const year = date.getFullYear();
	const month = date.getMonth() + 1;
	const day = date.getDate();
	const hours = date.getHours();
	const minutes = date.getMinutes();

	const d = `${year}.${month >= 10 ? month : `0${month}`}.${
		day >= 10 ? day : `0${day}`
	}`;
	const d2 = `${hours >= 10 ? hours : `0${hours}`}:${
		minutes >= 10 ? minutes : `0${minutes}`
	}`;
	return `${d}.${d2}`;
};
