import React from 'react';
import { useNavigate } from 'react-router-dom';
import { FcCheckmark } from 'react-icons/fc';
import { FaArrowRight } from 'react-icons/fa';
import { SwapResultBox, Form, HeaderDiv, StyleDiv, StyleButton } from './style';
import Layout from 'components/Layout';

function SwapResult() {
	const navigate = useNavigate();
	return (
		<Layout>
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
					<StyleButton className="gray" onClick={() => navigate('/swap')}>
						Back to Swap
					</StyleButton>
					<StyleButton
						className="yellow"
						onClick={() => navigate('/investments/history')}
					>
						거래내역 보러가기
					</StyleButton>
				</div>
			</Form>
		</Layout>
	);
}

export default SwapResult;
