import React, { useEffect, useRef, useState, memo } from 'react';
import { SubChartComponent } from './style';
import axios from 'axios';

const XAXIS_PADDING = 35;
const YAXIS_PADDING = 55;
const MAX_VALUE = 23000000;
const Y_TICK = 4;
const DURATION = 1000 * 60 * 60 * 2;
const EX_TIME = '00:00';
const TOP_PADDING = 15;

interface T {
	market: string;
	candle_date_time_utc: string;
	candle_date_time_kst: string;
	opening_price: number;
	high_price: number;
	low_price: number;
	trade_price: number;
	timestamp: number;
	candle_acc_trade_price: number;
	candle_acc_trade_volume: number;
	unit: number;
}

function SubChart() {
	const [data, setData] = useState<T[]>([]);
	useEffect(() => {
		axios
			.get(
				'https://api.upbit.com/v1/candles/minutes/1?market=KRW-BTC&count=200'
			)
			.then((res) => setData(res.data));
	}, []);
	useEffect(() => {
		const canvas = document.getElementById('canvas') as HTMLCanvasElement;
		const ctx = canvas.getContext('2d') as CanvasRenderingContext2D;
		const canvasWidth = canvas.clientWidth;
		const canvasHeight = canvas.clientHeight;
		const chartWidth = canvasWidth - XAXIS_PADDING;
		const chartHeight = canvasHeight - XAXIS_PADDING - TOP_PADDING;
		const xFormatWidth = ctx.measureText(EX_TIME).width;
		let endTime: number, startTime: number, xTimeInterval: number;

		const setXInterval = () => {
			let xPoint = 0;
			let timeInterval = 1000 * 60 * 12;
			while (true) {
				xPoint = (timeInterval / DURATION) * chartWidth;
				if (xPoint > xFormatWidth) break;
				timeInterval *= 2;
			}

			xTimeInterval = timeInterval;
		};

		const setTime = () => {
			endTime = Date.now() - 1000 * 60;
			startTime = endTime - DURATION;
			setXInterval();
		};

		const drawChart = () => {
			setTime();
			ctx.clearRect(0, 0, canvasWidth, canvasHeight);
			ctx.beginPath();
			ctx.moveTo(YAXIS_PADDING, TOP_PADDING);
			// draw Y axis
			ctx.lineTo(YAXIS_PADDING, chartHeight + TOP_PADDING);
			const yInterval = MAX_VALUE / Y_TICK;
			ctx.textAlign = 'right';
			ctx.textBaseline = 'middle';
			for (let i = 0, j = Y_TICK; i < Y_TICK; i++, j--) {
				const value = MAX_VALUE - 100000 * j;
				const pointVlaue = i * yInterval;
				const yPoint =
					TOP_PADDING + chartHeight - (pointVlaue / MAX_VALUE) * chartHeight;
				console.log(yPoint);
				ctx.fillText(value + '', YAXIS_PADDING - 5, yPoint);
			}

			// draw X axis
			ctx.lineTo(chartWidth, chartHeight + TOP_PADDING);
			ctx.stroke();

			ctx.save();
			ctx.beginPath();
			ctx.rect(YAXIS_PADDING, 0, chartWidth, canvasHeight);
			ctx.clip();

			let currentTime = startTime - (startTime % xTimeInterval);
			ctx.textBaseline = 'top';
			ctx.textAlign = 'center';
			while (currentTime < endTime + xTimeInterval) {
				const xPoint = ((currentTime - startTime) / DURATION) * chartWidth;
				const date = new Date(currentTime);
				const text = date.getHours() + ':' + date.getMinutes();
				ctx.fillText(text, xPoint, chartHeight + TOP_PADDING + 4);
				currentTime += xTimeInterval;
			}

			// draw data

			ctx.lineWidth = 2;
			[1, 2].forEach((v) => {
				data.forEach((datum, index) => {
					ctx.beginPath();
					const time = datum.timestamp;
					const d = 22000000;
					let highPrice, lowPrice, barWidth;
					if (v === 1) {
						barWidth = ((1000 * 60) / DURATION) * chartWidth * 0.1;
						highPrice = datum.high_price;
						lowPrice = datum.low_price;
					} else {
						barWidth = ((1000 * 60) / DURATION) * chartWidth * 0.65;
						highPrice = datum.trade_price;
						lowPrice = datum.opening_price;
						if (datum.opening_price < datum.trade_price) ctx.fillStyle = 'red';
						else ctx.fillStyle = 'royalblue';
					}
					const xPoint =
						YAXIS_PADDING + ((time - startTime) / DURATION) * chartWidth;
					const yPoint =
						TOP_PADDING +
						chartHeight -
						((highPrice - d) / (MAX_VALUE - d)) * chartHeight;
					const yLowPoint =
						TOP_PADDING +
						chartHeight -
						((lowPrice - d) / (MAX_VALUE - d)) * chartHeight;
					ctx.rect(
						xPoint - barWidth / 2,
						yPoint,
						barWidth,
						TOP_PADDING +
							chartHeight -
							yPoint -
							(TOP_PADDING + chartHeight - yLowPoint)
					);
					ctx.fill();
				});
			});
			ctx.stroke();
			ctx.restore();
			window.requestAnimationFrame(drawChart);
		};

		drawChart();
	}, [data]);
	return (
		<SubChartComponent>
			<canvas id="canvas" width="990" height="450"></canvas>
		</SubChartComponent>
	);
}

export default memo(SubChart);
