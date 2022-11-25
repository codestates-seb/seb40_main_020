import React, { useEffect, useState, useRef, memo } from 'react';
import { createChart } from 'lightweight-charts';
import axios from 'axios';
import { SubChartComponent } from './style';
import { useRecoilValue } from 'recoil';
import { coinDataState } from '../../../../store';

interface T {
	market?: string;
	candle_date_time_utc?: string;
	candle_date_time_kst?: string;
	opening_price?: number;
	high_price?: number;
	low_price?: number;
	trade_price?: number;
	timestamp?: number;
	candle_acc_trade_price?: number;
	candle_acc_trade_volume?: number;
	unit?: number;
}
interface DataType {
	time: number;
	open: number;
	high: number;
	low: number;
	close: number;
}
interface Props {
	code: string;
	time: number;
	chartSelector: number;
}

function SubChart({ code, time, chartSelector }: Props) {
	const { textColor, upColor, downColor } = {
		textColor: 'black',
		upColor: 'red',
		downColor: 'royalblue',
	};
	const chartContainerRef = useRef<any>(null);
	const [data, setData] = useState<DataType[]>([]);
	const [date, setDate] = useState(new Date());
	const [barsAfter, setBarsAfter] = useState(0);

	let chart: any;
	let newSeries: any;
	const api = async (type: string) => {
		const res = await axios.get(
			`https://api.upbit.com/v1/candles/minutes/${time}?market=${code}&count=200&to=${date.toISOString()}`
		);
		const newData = res.data
			.map((v: T) => {
				const obj: DataType = {
					time: Math.floor((v.timestamp as number) / 1000),
					open: v.opening_price as number,
					high: v.high_price as number,
					low: v.low_price as number,
					close: v.trade_price as number,
				};
				return obj;
			})
			.reverse();
		if (type === 'date') {
			for (let i = 0; i < data.length; i++) {
				if (newData[newData.length - 1].time >= data[0].time) {
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
		api('date');
	}, [date]);
	useEffect(() => {
		api('time');
	}, [time, code]);

	useEffect(() => {
		if (chartSelector === 1) {
			const myPriceFormatter = (p: number) => {
				if (p > 100) return p;
				else return p.toFixed(2);
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
	}, [data, chartSelector, barsAfter]);

	const mouseHandler = () => {
		const barsInfo = newSeries?.barsInLogicalRange(
			chart.timeScale().getVisibleLogicalRange()
		);
		if (barsInfo?.barsBefore <= 10) {
			const d = 1000 * 60 * 200;
			setDate(new Date(+date - d));
			setBarsAfter(barsInfo.barsAfter);
		}
	};
	return (
		<SubChartComponent className="chart-wrapper">
			<div
				ref={chartContainerRef}
				onMouseUp={mouseHandler}
				onWheel={mouseHandler}
			></div>
		</SubChartComponent>
	);
}

export default memo(SubChart);
