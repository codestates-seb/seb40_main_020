import React, { useState } from 'react';
import { ChartListComponent } from './style';
import Chart from '../Chart';
import SubChart from '../SubChart';

interface Props {
	symbol: {
		coin: string;
		code: string;
		symbol: string;
	};
}
function ChartList({ symbol }: Props) {
	const [time, setTime] = useState(1);
	const [chartSelector, setChartSelector] = useState(1);
	const minArray = [1, 3, 5, 15, 30, 60, 240];
	const chartArray = [1, 2];
	const selectHandler = (
		s: number,
		set: React.Dispatch<React.SetStateAction<number>>
	) => {
		set(s);
	};

	return (
		<ChartListComponent className="chart-wrapper" chartSelector={chartSelector}>
			<div className="min-wrapper">
				{chartSelector === 1 && (
					<div>
						<div>Time</div>
						{minArray.map((v, i) => (
							<div
								key={i}
								onClick={() => selectHandler(v, setTime)}
								className={time === v ? 'select m' : 'm'}
							>
								{v}min
							</div>
						))}
					</div>
				)}
				<div>
					{chartArray.map((v, i) => (
						<div
							key={i}
							onClick={() => selectHandler(v, setChartSelector)}
							className={chartSelector === v ? 'select m' : 'm'}
						>
							chart {v}
						</div>
					))}
				</div>
			</div>
			{chartSelector === 1 ? (
				<SubChart
					code={symbol.code}
					time={time}
					chartSelector={chartSelector}
				/>
			) : (
				<Chart symbol={symbol.symbol} />
			)}
		</ChartListComponent>
	);
}

export default ChartList;
