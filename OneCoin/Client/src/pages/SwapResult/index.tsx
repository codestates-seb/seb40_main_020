import React from 'react';
import { FcCheckmark } from 'react-icons/fc';
import { FaArrowRight } from 'react-icons/fa';
import { SwapResultBox, Form, HeaderDiv, StyleDiv, StyleButton } from './style';

function Swap() {
	return (
		<SwapResultBox>
			<Form>
				<StyleDiv>
					<HeaderDiv>스왑 성공!</HeaderDiv>
					<FcCheckmark size={60} />
				</StyleDiv>
				<StyleDiv>
					<div>
						<StyleDiv>From</StyleDiv>
						<StyleDiv>0.1 BTC</StyleDiv>
					</div>
					<FaArrowRight size={50} />
					<div>
						<StyleDiv>To</StyleDiv>
						<StyleDiv>0.1 AXS</StyleDiv>
					</div>
				</StyleDiv>
				<div>
					<StyleButton className="gray">Back to Swap</StyleButton>
					<StyleButton className="yellow">거래내역 보러가기</StyleButton>
				</div>
			</Form>
		</SwapResultBox>
	);
}

export default Swap;
