import styled from 'styled-components';

export const ChartComponent = styled.div`
	width: 100%;
	height: 100%;
	#tradingview_widget_wrapper {
		> div {
			:nth-last-child(1) {
				display: none;
			}
		}
	}
	margin-bottom: 12px;
`;
