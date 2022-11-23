import React, { useEffect, useState, useRef } from 'react';
import { createChart } from 'lightweight-charts';
import axios from 'axios';

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

function SubChart() {
	const {
		backgroundColor,
		lineColor,
		textColor,
		areaTopColor,
		areaBottomColor,
		upColor,
		downColor,
	} = {
		backgroundColor: 'white',
		lineColor: '#2962FF',
		textColor: 'black',
		areaTopColor: '#2962FF',
		upColor: 'red',
		downColor: 'royalblue',
		areaBottomColor: 'rgba(41, 98, 255, 0.28)',
	};
	const chartContainerRef = useRef<any>(null);
	const [data, setData] = useState<DataType[]>([]);
	const [date, setDate] = useState(new Date());
	const [time, setTime] = useState(1);
	useEffect(() => {
		axios
			.get(
				`https://api.upbit.com/v1/candles/minutes/${time}?market=KRW-BTC&count=200&to=${date.toISOString()}`
			)
			.then((res) => {
				const newData = res.data
					.map((v: T) => {
						const obj: DataType = {
							time: ((v.timestamp as number) / 1000) * time,
							open: v.opening_price as number,
							high: v.high_price as number,
							low: v.low_price as number,
							close: v.trade_price as number,
						};
						return obj;
					})
					.reverse();
				setData([...newData, ...data]);
			});
	}, [date, time]);

	useEffect(() => {
		const myPriceFormatter = (p: number) => {
			if (p > 100) return p;
			else return p.toFixed(2);
		};

		const chart: any = createChart(chartContainerRef.current, {
			layout: {
				textColor,
			},
			width: 990,
			height: 450,
		});
		chart.applyOptions({
			localization: {
				priceFormatter: myPriceFormatter,
			},
		});
		chart.timeScale({ timeVisible: true }).applyOptions({ timeVisible: true });
		const newSeries = chart.addCandlestickSeries({
			upColor: upColor,
			borderUpColor: upColor,
			downColor: downColor,
			borderDownColor: downColor,
			wickColor: 'black',
		});
		newSeries.setData(data);

		const handleResize = () => {
			chart.applyOptions();
		};
		window.addEventListener('resize', handleResize);
		console.log(chart.barsBefore);
		return () => {
			window.removeEventListener('resize', handleResize);

			chart.remove();
		};
	}, [
		data,
		backgroundColor,
		lineColor,
		textColor,
		areaTopColor,
		areaBottomColor,
	]);

	return (
		<>
			<select
				onChange={(e: React.ChangeEvent<HTMLSelectElement>) => {
					setTime(+e.target.value);
					console.log(e.target.value);
				}}
			>
				<option value={1}>1M</option>
				<option value={3}>3M</option>
				<option value={5}>5M</option>
				<option value={15}>15M</option>
				<option value={30}>30M</option>
				<option value={60}>1H</option>
			</select>
			<div ref={chartContainerRef}></div>;
			<button
				onClick={() => {
					const d = 1000 * 60 * 200;
					setDate(new Date(+date - d));
				}}
			>
				zz
			</button>
		</>
	);
}

export default SubChart;
