import React, { useEffect, useState, useRef, memo } from 'react';
import { createChart } from 'lightweight-charts';
import { SubChartComponent } from './style';
import { useRecoilValue } from 'recoil';
import { coinDataState } from '../../../../store';
import { getChartData } from '../../../../api/exchange';
import { ChartData } from '../../../../utills/types';

interface Props {
	code: string;
	time: number;
	chartSelector: number;
}

function SubChart({ code, time, chartSelector }: Props) {
	const UPDATE_INITIAL_DATA = {
		time: 0,
		open: '0',
		high: '0',
		low: '0',
		close: '0',
	};
	const coinData = useRecoilValue(coinDataState);

	const [updateData, setUpdateData] = useState<ChartData>(UPDATE_INITIAL_DATA);
	const { textColor, upColor, downColor } = {
		textColor: 'black',
		upColor: 'red',
		downColor: 'royalblue',
	};
	const chartContainerRef = useRef<any>(null);
	const [data, setData] = useState<ChartData[]>([]);
	const [date, setDate] = useState(new Date());
	const [barsAfter, setBarsAfter] = useState(0);

	let chart: any;
	let newSeries: any;
	useEffect(() => {
		const coin = coinData.filter((v) => v.code === code)[0];
		if (coin?.ticker?.code as string) {
			const tradePrice = coin?.ticker?.trade_price as string;
			const lastData = data[data.length - 1];
			const unl = Object.values(updateData);
			if (unl.includes(undefined) || unl.includes(0)) {
				setUpdateData({
					time: lastData?.time + 1,
					open: lastData?.open,
					high: lastData?.high,
					low: lastData?.low,
					close: lastData?.close,
				});
			} else {
				const newUpdateData = { ...updateData };
				newUpdateData.time = Number(coin.ticker?.timestamp as string) / 1000;
				newUpdateData.close = tradePrice;
				newUpdateData.low =
					newUpdateData.low < tradePrice ? newUpdateData.low : tradePrice;
				newUpdateData.high =
					newUpdateData.high > tradePrice ? newUpdateData.high : tradePrice;
				setUpdateData(newUpdateData);
			}
		}
	}, [coinData]);

	const chartData = async (type: string) => {
		const res = await getChartData(time, code, date.toISOString());
		const newData: ChartData[] = [];
		for (let i = res.length - 1; i >= 0; i--) {
			const obj: ChartData = {
				time: Math.floor(Number(res[i].timestamp as string) / 1000),
				open: res[i].opening_price as string,
				high: res[i].high_price as string,
				low: res[i].low_price as string,
				close: res[i].trade_price as string,
			};
			newData.push(obj);
		}

		if (type === 'date') {
			for (let i = 0; i < data.length; i++) {
				if (Number(newData[newData.length - 1].time) >= Number(data[0].time)) {
					newData.pop();
				} else break;
			}
			await setData([...newData, ...data]);
		} else {
			await setData([...newData]);
			setBarsAfter(0);
		}
	};
	useEffect(() => {
		chartData('date');
	}, [date]);
	useEffect(() => {
		chartData('time');
	}, [time, code]);
	useEffect(() => {
		setUpdateData(UPDATE_INITIAL_DATA);
	}, [code]);
	useEffect(() => {
		if (chartSelector === 1) {
			const myPriceFormatter = (p: string) => {
				if (Number(p) > 100) return p;
				else return Number(p).toFixed(2);
			};

			chart = createChart(chartContainerRef.current, {
				layout: {
					textColor,
				},
				width: 998,
				height: 428,
			});
			chart.applyOptions({
				localization: {
					priceFormatter: myPriceFormatter,
				},
			});
			chart.timeScale().applyOptions({ timeVisible: true });
			newSeries = chart.addCandlestickSeries({
				upColor: upColor,
				borderUpColor: upColor,
				downColor: downColor,
				borderDownColor: downColor,
				wickColor: 'black',
			});
			newSeries.setData(data);
			const unl = Object.values(updateData);
			if (!(unl.includes(undefined) || unl.includes('0'))) {
				const prevT = new Date(data[data.length - 1].time * 1000).getMinutes();
				const nxtT = new Date(updateData.time * 1000).getMinutes();
				if (nxtT - prevT === time) {
					setData([...data, updateData]);
					newSeries.setData([...data, updateData]);
					setUpdateData({
						time: updateData.time + 1,
						open: updateData.close,
						high: updateData.close,
						low: updateData.close,
						close: updateData.close,
					});
				} else {
					newSeries.update(updateData);
				}
			}
			chart.timeScale({
				timeVisible: true,
				secondsVisible: false,
			});
			chart.timeScale().scrollToPosition(-barsAfter, false);
			const handleResize = () => {
				chart.applyOptions();
			};
			window.addEventListener('resize', handleResize);
			return () => {
				window.removeEventListener('resize', handleResize);
				chart.remove();
			};
		}
	}, [data, chartSelector, barsAfter, coinData, updateData, code]);

	const mouseHandler = () => {
		const barsInfo = newSeries?.barsInLogicalRange(
			chart.timeScale().getVisibleLogicalRange()
		);

		if (barsInfo?.barsBefore <= 10) {
			const d = 1000 * 60 * 200;
			setDate(new Date(+date - d));
		}
		setBarsAfter(barsInfo.barsAfter);
	};
	return (
		<SubChartComponent className="chart-wrapper">
			<div ref={chartContainerRef} onMouseUp={mouseHandler}></div>
		</SubChartComponent>
	);
}

export default memo(SubChart);
