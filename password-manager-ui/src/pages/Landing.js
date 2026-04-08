import { motion } from "framer-motion";
import "./Landing.css";

function Landing({ goToLogin }) {

  return (
    <div className="landing">

      {/* HERO */}
      <motion.section
        className="hero"
        initial={{ opacity: 0, y: -40 }}
        animate={{ opacity: 1, y: 0 }}
      >
        <h1>🛡 AegisVault</h1>
        <p>Secure your digital identity with next-generation encryption</p>

        <button className="cta" onClick={goToLogin}>
          Get Started →
        </button>
      </motion.section>

      {/* FEATURES */}
      <section className="features">
        <div className="feature">
          <h3>🔐 AES Encryption</h3>
          <p>Industry-grade encryption ensures your passwords remain private.</p>
        </div>

        <div className="feature">
          <h3>🛡 Zero Trust Security</h3>
          <p>No one can access your vault without your credentials.</p>
        </div>

        <div className="feature">
          <h3>⚡ Fast Access</h3>
          <p>Retrieve your credentials instantly with a smooth interface.</p>
        </div>
      </section>

      {/* CYBER SECURITY INFO */}
      <section className="info">
        <h2>Why Cybersecurity Matters</h2>
        <p>
          In today’s digital world, passwords are the keys to your identity.
          Data breaches, phishing attacks, and credential leaks are increasing every day.
        </p>

        <p>
          AegisVault is designed to protect your credentials using modern encryption
          techniques, ensuring that even if data is exposed, it remains unreadable.
        </p>
      </section>

      {/* HOW IT WORKS */}
      <section className="info dark">
        <h2>How AegisVault Protects You</h2>

        <ul>
          <li>🔑 Passwords are encrypted before storage</li>
          <li>🧠 Keys are derived securely using your account password</li>
          <li>🔐 Only you can decrypt your data</li>
          <li>⚡ Fast and secure access anytime</li>
        </ul>
      </section>

      {/* CALL TO ACTION */}
      <section className="cta-section">
        <h2>Take Control of Your Security</h2>
        <p>Start managing your passwords securely today.</p>

        <button className="cta" onClick={goToLogin}>
          Create Account
        </button>
      </section>

    </div>
  );
}

export default Landing;