import React from 'react';
import { FooterComponent } from './syle';
import { useNavigate } from 'react-router-dom';
import { AiFillGithub } from 'react-icons/ai';
import logo from '../../assets/images/one.png';
import { FOOTER_LIST } from '../../utills/constants/footer';

function Footer() {
	const navigate = useNavigate();

	return (
		<FooterComponent>
			<div className="content">
				<div>
					<img src={logo} onClick={() => navigate('/')} />
					<a href={FOOTER_LIST.repository} target="_blank" rel="noreferrer">
						Github Repository
					</a>
				</div>
				<div>{FOOTER_LIST.copyright}</div>
				<div>
					<div className="rep">
						<div>FE</div>
						{FOOTER_LIST.fe.map((v, i) => (
							<a href={v.repository} key={i} target="_blank" rel="noreferrer">
								<div>
									<AiFillGithub />
								</div>
								<div>{v.name}</div>
							</a>
						))}
					</div>
					<div className="rep">
						<div>BE</div>
						{FOOTER_LIST.be.map((v, i) => (
							<a href={v.repository} key={i} target="_blank" rel="noreferrer">
								<div>
									<AiFillGithub />
								</div>
								<div>{v.name}</div>
							</a>
						))}
					</div>
				</div>
			</div>
		</FooterComponent>
	);
}

export default Footer;
